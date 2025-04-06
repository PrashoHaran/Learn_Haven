package com.example.learnhaven;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class ebook_register extends AppCompatActivity {

    private EditText bookTitle, authorName, genre, bookDescription, ratings;
    private Button btnUpload, btnAdd, btnUpdate, btnDelete, btnView;
    private FirebaseFirestore db;
    private StorageReference storageReference;
    private Uri selectedImageUri;
    private String coverImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ebook_register);

        // Initialize Firebase Firestore and Storage
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        // Initialize views
        bookTitle = findViewById(R.id.book_title);
        authorName = findViewById(R.id.author_name);
        genre = findViewById(R.id.genre);
        bookDescription = findViewById(R.id.book_description);
        ratings = findViewById(R.id.ratings);
        btnUpload = findViewById(R.id.btn_upload);
        btnAdd = findViewById(R.id.btn_add);
        btnUpdate = findViewById(R.id.btn_update);
        btnDelete = findViewById(R.id.btn_delete);
        btnView = findViewById(R.id.btn_view);

        // Back button functionality
        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ebook_register.this, ebook.class);
            startActivity(intent);
            finish();
        });

        // Upload Cover Image
        btnUpload.setOnClickListener(v -> {
            openImagePicker();
        });

        // Add (Create) Book Entry
        btnAdd.setOnClickListener(v -> {
            addBook();
        });

        // Update Book Details
        btnUpdate.setOnClickListener(v -> {
            updateBook();
        });

        // Delete Book Entry
        btnDelete.setOnClickListener(v -> {
            deleteBook();
        });

        // View All Books (Read functionality)
        btnView.setOnClickListener(v -> {
            viewAllBooks();
        });
    }

    // Method to open image picker
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 100);  // 100 is the request code for image selection
    }

    // Handling image selection result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            Toast.makeText(this, "Image Selected", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to add a new eBook (Create functionality)
    private void addBook() {
        String title = bookTitle.getText().toString().trim();
        String author = authorName.getText().toString().trim();
        String bookGenre = genre.getText().toString().trim();
        String description = bookDescription.getText().toString().trim();
        String rating = ratings.getText().toString().trim();

        if (title.isEmpty() || author.isEmpty() || bookGenre.isEmpty() || description.isEmpty() || rating.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedImageUri != null) {
            uploadImageToFirebase();
        } else {
            Toast.makeText(this, "Please select a cover image", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to upload the cover image to Firebase Storage
    private void uploadImageToFirebase() {
        // Create a storage reference with a unique name for the image
        StorageReference imageRef = storageReference.child("ebook_images/" + System.currentTimeMillis() + ".jpg");

        imageRef.putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // After successful upload, get the image URL
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        coverImageUrl = uri.toString();
                        saveBookDataToFirestore();
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ebook_register.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                });
    }

    // Method to save the book data to Firebase Firestore
    private void saveBookDataToFirestore() {
        String title = bookTitle.getText().toString().trim();
        String author = authorName.getText().toString().trim();
        String bookGenre = genre.getText().toString().trim();
        String description = bookDescription.getText().toString().trim();
        String rating = ratings.getText().toString().trim();

        Map<String, Object> bookData = new HashMap<>();
        bookData.put("title", title);
        bookData.put("author", author);
        bookData.put("genre", bookGenre);
        bookData.put("description", description);
        bookData.put("ratings", rating);
        bookData.put("coverImageUrl", coverImageUrl);

        db.collection("ebooks")
                .add(bookData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(ebook_register.this, "Book added successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ebook_register.this, "Error adding book", Toast.LENGTH_SHORT).show();
                });
    }

    // Method to update the book details
    private void updateBook() {
        String title = bookTitle.getText().toString().trim();
        String author = authorName.getText().toString().trim();
        String bookGenre = genre.getText().toString().trim();
        String description = bookDescription.getText().toString().trim();
        String rating = ratings.getText().toString().trim();

        if (title.isEmpty() || author.isEmpty() || bookGenre.isEmpty() || description.isEmpty() || rating.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("ebooks")
                .whereEqualTo("title", title)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            DocumentReference docRef = db.collection("ebooks").document(document.getId());

                            Map<String, Object> updatedData = new HashMap<>();
                            updatedData.put("title", title);
                            updatedData.put("author", author);
                            updatedData.put("genre", bookGenre);
                            updatedData.put("description", description);
                            updatedData.put("ratings", rating);

                            // Update coverImageUrl only if a new one has been selected
                            if (coverImageUrl != null && !coverImageUrl.isEmpty()) {
                                updatedData.put("coverImageUrl", coverImageUrl);
                            }

                            docRef.set(updatedData, SetOptions.merge())
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(ebook_register.this, "Book updated successfully!", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(ebook_register.this, "Error updating book", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        Toast.makeText(ebook_register.this, "Book not found for update", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ebook_register.this, "Error finding book to update", Toast.LENGTH_SHORT).show();
                });
    }


    // Method to delete a book entry
    private void deleteBook() {
        String titleToDelete = bookTitle.getText().toString().trim();

        if (titleToDelete.isEmpty()) {
            Toast.makeText(this, "Enter the book title to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("ebooks")
                .whereEqualTo("title", titleToDelete)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            db.collection("ebooks").document(document.getId())
                                    .delete()
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(ebook_register.this, "Book deleted successfully!", Toast.LENGTH_SHORT).show();
                                        clearFields();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(ebook_register.this, "Error deleting book", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        Toast.makeText(ebook_register.this, "Book not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ebook_register.this, "Error finding book", Toast.LENGTH_SHORT).show();
                });
    }

    private void clearFields() {
        bookTitle.setText("");
        authorName.setText("");
        genre.setText("");
        bookDescription.setText("");
        ratings.setText("");
    }


    // Method to view all books
    // Method to view all books in a dialog
    private void viewAllBooks() {
        db.collection("ebooks")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        StringBuilder booksList = new StringBuilder();

                        // Loop through the documents and append each book's details
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String title = document.getString("title");
                            String author = document.getString("author");
                            String genre = document.getString("genre");
                            String description = document.getString("description");
                            String rating = document.getString("ratings");

                            // Add the book details to the string
                            booksList.append("Title: ").append(title).append("\n")
                                    .append("Author: ").append(author).append("\n")
                                    .append("Genre: ").append(genre).append("\n")
                                    .append("Description: ").append(description).append("\n")
                                    .append("Ratings: ").append(rating).append("\n\n");
                        }

                        // Show the book details in a dialog
                        showBooksDialog(booksList.toString());
                    } else {
                        Toast.makeText(ebook_register.this, "No books found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ebook_register.this, "Error fetching books", Toast.LENGTH_SHORT).show();
                });
    }

    // Method to show the books in a dialog
    private void showBooksDialog(String booksList) {
        // Create a dialog
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ebook_register.this);
        builder.setTitle("All Books");

        // Set the text to display in the dialog
        builder.setMessage(booksList);

        // Set the positive button for closing the dialog
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

        // Create and show the dialog
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

}
