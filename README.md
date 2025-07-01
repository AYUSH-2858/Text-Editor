# 📝 Java Swing Text Editor

A modern, customizable text editor built using **Java Swing**, featuring line numbers, undo/redo, dark mode, and keyboard shortcuts—designed to feel similar to lightweight IDEs like VS Code.

---

## 🚀 Features

- ✅ **Basic Editing**
  - Cut / Copy / Paste
  - Undo / Redo (with keyboard shortcuts)
  - Font family & size selection
  - Font color chooser

- 📁 **File Operations**
  - Open `.txt` or other plain text files
  - Save edited text to file
  - Keyboard shortcuts: `Ctrl+O`, `Ctrl+S`, `Ctrl+Q`

- 🌗 **Theme Modes**
  - Light Mode ☀️
  - Dark Mode 🌙 (with custom background and caret color)

- 🧠 **Editor Enhancements**
  - Line Numbers
  - Current line highlight
  - Horizontal stroke lines like VS Code
  - Toggleable Line Wrapping

---

## 💻 Technologies Used

- Java 8+ or later
- Swing (Java GUI toolkit)
- `UndoManager`, `JFileChooser`, `JColorChooser`, `JTextArea`, `JScrollPane`, etc.

---

## ⌨️ Keyboard Shortcuts

| Action        | Shortcut           |
|---------------|--------------------|
| Open File     | `Ctrl + O`         |
| Save File     | `Ctrl + S`         |
| Exit          | `Ctrl + Q`         |
| Undo          | `Ctrl + Z`         |
| Redo          | `Ctrl + Y`         |
| Cut           | `Ctrl + X`         |
| Copy          | `Ctrl + C`         |
| Paste         | `Ctrl + V`         |

---

## 🛠️ How to Run

1. Make sure you have **Java installed** (JDK 8 or above).
2. Compile both files:
   ```bash
   javac TextEditor.java TextLineNumber.java
