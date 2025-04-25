📄 DocuMate
DocuMate is a modern Android application built using Kotlin and XML, following the MVVM architecture with a clean and maintainable codebase. It provides a seamless experience for document handling, user authentication, and local data management with Room.

🚀 Features
🔐 Google Sign-In Authentication
Secure and easy login using Google accounts.

🌐 API Integration (Retrofit)
Fetch and post data from/to a remote server with robust error handling.

💾 Room Database (CRUD Operations)
Efficient local storage with full Create, Read, Update, and Delete functionality.

📷 Image Picker (Camera & Gallery)
Capture new photos or select from the device's gallery.

📄 PDF Viewer
Smooth and responsive rendering of PDF files.

💡 MVVM Architecture
Separates concerns between UI and business logic for scalability and testability.

✅ Clean Code Practices
Modular, readable, and reusable code with proper naming conventions and architecture boundaries.

🧰 Tech Stack

Layer	Libraries / Tools
UI	XML Layouts, ViewBinding
Language	Kotlin
Architecture	MVVM (Model - View - ViewModel)
Networking	Retrofit, Gson
Local Storage	Room Database
Authentication	Firebase Auth with Google Sign-In
Image Picker	Android Media Store, Intents
PDF Viewing	Android PdfRenderer / third-party viewer libs
📷 Screenshots
Coming soon...

📁 Project Structure
bash
Copy
Edit
DocuMate/
│
├── data/
│   ├── api/           # Retrofit setup and API interfaces
│   ├── db/            # Room database, entities, DAO
│   └── repository/    # Repository pattern for data access
│
├── ui/
│   ├── view/          # Activities and Fragments
│   └── viewmodel/     # ViewModel classes
│
├── utils/             # Helpers and utility classes
└── MainApplication.kt # Application class setup
🔧 Setup Instructions
Clone the Repository

bash
Copy
Edit
git clone https://github.com/yourusername/DocuMate.git
Open in Android Studio

Configure Firebase for Google Sign-In
Add your google-services.json in the app/ directory.

Build and Run on Emulator or Device

💬 Contributions
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

📜 License
This project is licensed under the MIT License. See the LICENSE file for details.
