package com.nexus.services;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

public class FirebaseService {
    private static final Logger LOGGER = Logger.getLogger(FirebaseService.class.getName());
    private static volatile FirebaseService instance;
    private Firestore db;

    private FirebaseService() {
        try {
            Path keyPath = resolveServiceAccountPath();
            FileInputStream serviceAccount = new FileInputStream(keyPath.toFile());
            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
            FirebaseApp.initializeApp(options);
            this.db = FirestoreClient.getFirestore();
        } catch (IOException | RuntimeException e) {
            LOGGER.log(Level.SEVERE, "Firebase initialization failed", e);
        }
    }

    @SuppressWarnings("DoubleCheckedLocking")
    public static FirebaseService getInstance() {
        if (instance == null) {
            synchronized (FirebaseService.class) {
                if (instance == null) instance = new FirebaseService();
            }
        }
        return instance;
    }

    private Path resolveServiceAccountPath() throws IOException {
        String fromEnv = System.getenv("FIREBASE_SERVICE_ACCOUNT");
        if (fromEnv != null && !fromEnv.isBlank()) {
            Path envPath = Paths.get(fromEnv.trim()).toAbsolutePath().normalize();
            if (Files.exists(envPath)) {
                return envPath;
            }
        }

        Path[] candidates = new Path[] {
            Paths.get("serviceAccountKey.json"),
            Paths.get("nexus-pricing-promotions-module", "serviceAccountKey.json"),
            Paths.get("..", "serviceAccountKey.json")
        };

        for (Path candidate : candidates) {
            Path normalized = candidate.toAbsolutePath().normalize();
            if (Files.exists(normalized)) {
                return normalized;
            }
        }

        throw new IOException("serviceAccountKey.json not found in supported locations");
    }

    public Firestore getDb() { return db; }
}