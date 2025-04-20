# Dictionary Client (Java Swing GUI)

This is the client-side GUI application for interacting with the Dictionary Server over TCP sockets. It allows users to query, add, remove, or update words and meanings using a graphical interface built with Java Swing.

---

## ✅ Features

- 📋 **Dropdown command selection** (`addword`, `removeword`, `querymeanings`, `addmeaning`, `updatemeaning`)
- 📝 **Text inputs** for word, meaning(s), old and new meaning
- 📡 **Socket communication** with the server
- 💬 **JSON-based protocol** using Google's Gson library
- 💻 **Clean, responsive GUI** designed with IntelliJ's Swing UI Designer

---

## 🚀 How to Run

1. Ensure the **Dictionary Server** is already running on port `3005`.

2. Open the project in IntelliJ.

3. Navigate to `Main.java` (or the class with your `main()` method) and click run
