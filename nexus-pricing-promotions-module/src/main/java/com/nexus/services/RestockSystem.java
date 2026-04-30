package com.nexus.services;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;

@Service
public class RestockSystem implements ManagerSubject {

    private final List<ManagerObserver> observers = new ArrayList<>();
    private final List<String> notificationLogs = new ArrayList<>();
    private final Path csvAuditPath = Paths.get("restock_requests_audit.csv");
    private static final DateTimeFormatter TS_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public synchronized void attach(ManagerObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public synchronized void detach(ManagerObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String branch, String item) {
        List<ManagerObserver> observersSnapshot;
        synchronized (this) {
            observersSnapshot = new ArrayList<>(observers);
        }
        for (ManagerObserver observer : observersSnapshot) {
            observer.update(branch, item);
        }
    }

    public void createRequest(String branch, String item) {
        requestRestock(branch, item);
    }

    public void requestRestock(String branch, String item) {
        String safeBranch = (branch == null || branch.isBlank()) ? "Unknown Branch" : branch.trim();
        String safeItem = (item == null || item.isBlank()) ? "Unknown Item" : item.trim();

        logEvent(safeBranch, safeItem, "REQUEST", "", safeBranch + " requested " + safeItem);
        notifyObservers(safeBranch, safeItem);
        fulfillRestock(safeBranch, safeItem);
    }

    private void fulfillRestock(String branch, String item) {
        try {
            Firestore db = FirebaseService.getInstance().getDb();
            if (db == null) {
                return;
            }

            for (DocumentSnapshot doc : db.collection("inventory").get().get().getDocuments()) {
                String docId = doc.getId();
                String docName = doc.getString("name");
                String docWarehouse = doc.getString("warehouse");
                String docWarehouseAlt = doc.getString("warehouse_name");

                boolean matchesItem = matchesValue(item, docId) || matchesValue(item, docName);
                boolean matchesBranch = matchesValue(branch, docWarehouse) || matchesValue(branch, docWarehouseAlt) || "All".equalsIgnoreCase(branch);

                if (!matchesItem || !matchesBranch) {
                    continue;
                }

                Long thresholdValue = doc.getLong("threshold");
                Long quantityValue = doc.getLong("quantity");
                int threshold = thresholdValue == null ? 0 : thresholdValue.intValue();
                int currentQuantity = quantityValue == null ? 0 : quantityValue.intValue();
                int replenishedQuantity = Math.max(currentQuantity, threshold + 10);

                db.collection("inventory").document(docId).update("quantity", replenishedQuantity).get();
                logEvent(branch, item, "RESTOCKED", "", item + " restocked to " + replenishedQuantity + " units");
                return;
            }

            logEvent(branch, item, "RESTOCKED", "", item + " restock recorded, but no matching inventory item was found");
        } catch (Exception e) {
            logEvent(branch, item, "RESTOCK_FAILED", "", "Failed to update inventory for " + item + ": " + e.getMessage());
        }
    }

    private boolean matchesValue(String expected, String actual) {
        if (expected == null || actual == null) {
            return false;
        }

        String normalizedExpected = expected.trim().toLowerCase();
        String normalizedActual = actual.trim().toLowerCase();
        return normalizedExpected.equals(normalizedActual) || normalizedActual.contains(normalizedExpected);
    }

    public synchronized void addLog(String log) {
        notificationLogs.add(log);
        if (notificationLogs.size() > 200) {
            notificationLogs.remove(0);
        }
    }

    public void logEvent(String branch, String item, String status, String supplier, String message) {
        String safeBranch = (branch == null || branch.isBlank()) ? "Unknown Branch" : branch.trim();
        String safeItem = (item == null || item.isBlank()) ? "Unknown Item" : item.trim();
        String safeStatus = (status == null || status.isBlank()) ? "UNKNOWN" : status.trim();
        String safeSupplier = (supplier == null) ? "" : supplier.trim();
        String safeMessage = (message == null || message.isBlank()) ? "-" : message.trim();

        addLog(safeMessage);
        appendCsvRow(safeBranch, safeItem, safeStatus, safeSupplier, safeMessage);
    }

    private synchronized void appendCsvRow(String branch, String item, String status, String supplier, String message) {
        try {
            ensureCsvHeader();
            String timestamp = LocalDateTime.now().format(TS_FORMAT);
            String row = String.join(",",
                    csv(timestamp),
                    csv(branch),
                    csv(item),
                    csv(status),
                    csv(supplier),
                    csv(message)) + System.lineSeparator();

            Files.writeString(csvAuditPath, row, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception ignored) {
            // Do not block the workflow if filesystem logging is unavailable.
        }
    }

    private void ensureCsvHeader() throws Exception {
        if (!Files.exists(csvAuditPath)) {
            String header = "timestamp,branch,item,status,supplier,message" + System.lineSeparator();
            Files.writeString(csvAuditPath, header, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }
    }

    private String csv(String value) {
        String normalized = (value == null) ? "" : value;
        return "\"" + normalized.replace("\"", "\"\"") + "\"";
    }

    public synchronized List<String> getNotificationLogs() {
        return Collections.unmodifiableList(new ArrayList<>(notificationLogs));
    }
}
