# Skillo Selenium Automation Project

This repository contains a UI automation framework for the **Skillo** social media platform. The project is built using **Java 17**, **Selenium WebDriver**, and **TestNG**, implementing the **Page Object Model (POM)** design pattern.

---

## 📂 Project Structure

```text
SkilloJavaSeleniumProject/
├── pom.xml                      # Maven configuration and dependencies
├── testng.xml                   # Test suite configuration and listeners
└── src/
    ├── main/java/skillo.automation/
    │   ├── BasePage.java        # Shared utilities (Waits, PageFactory init)
    │   ├── LoginPage.java       # Login/Authentication page objects
    │   ├── HomePage.java        # Feed and navigation logic
    │   └── ProfilePage.java     # User profile and post management logic
    ├── resources/
    │   ├── screenshots/         # Directory for failure screenshots (auto-generated)
    │   └── laleta.jpg           # Sample asset for post upload tests
    └── test/java/skillo.automation/
        ├── listeners/
        │   └── ScreenshotListener.java # Failure capture logic
        └── tests/
            ├── BaseTest.java    # Browser lifecycle management
            ├── LoginTests.java  # Auth flows and logout
            ├── HomeTests.java   # Feed interactions & like sync
            ├── PostTests.java   # Post creation and deletion
            └── ProfileTests.java # Data integrity and gallery checks

```
# 🏗️ Architecture Overview

### **Page Object Model (POM)**
The framework maintains a strict separation between **Test Logic** (the *"What"*) and **Page Locators/Interactions** (the *"How"*). This ensures that if the UI changes, updates are only needed in one place, making the suite highly maintainable.

### **Reporting & Failure Analysis**
* **📸 Screenshot Listener:** A custom TestNG listener automatically captures the browser state at the exact moment of any test failure.
* **📜 Detailed Logging:** The framework provides clear console output for every execution step (e.g., `Step: Entering credentials...`), facilitating easy debugging.

---

# 🧪 Key Test Scenarios

* **🔐 Auth Security (Login):** Comprehensive tests for successful login, secure logout, and proper error handling for incorrect credentials.
* **🖼️ Post Lifecycle (Post):** Automates the entire flow of uploading an image, adding a caption, and verifying its successful publication.
* **📊 Data Integrity (Profile):** A critical check that compares the numeric **"Post Count"** in the profile header with the actual number of post elements present in the gallery.
* **❤️ Like Synchronization (Home):** Validates that the like count updates correctly and matches between the *Home Feed* and the *Post Modal*.

---

# ⚠️ Known Issues Identified by Tests

The current automation suite has successfully identified and documented the following defects in the application:

1.  **Bug: Like Sync Mismatch** – The modal like count occasionally fails to synchronize with the feed count (e.g., *Feed displays 8, while Modal shows 7*).
2.  **Bug: Profile Metadata Error** – The profile header displays an incorrect total post count (e.g., *Header claims 42 posts, but only 1 exists in the gallery*).

---

# 🚀 Execution Guide

### **Running via Maven**
To execute the entire regression suite and generate results:
```bash
mvn test
```
### **Running Specific Tests**    

To run only a specific test category:
```bash
mvn test -Dtest=HomeTests
```     

### Analyzing Results

* **Console Output**: Review the logs for DEBUG, SUCCESS, or ERROR messages.

* **Visual Evidence**: Check the screenshots folder for images captured during failed assertions to identify the root cause of the UI mismatch.

### 🛠️ Tech Stack

* **Java 17**: Programming Language
* **Selenium WebDriver 4.41.0**: Browser Automation Engine
* **TestNG 7.12.0**: Test Runner and Assertions
* **WebDriverManager 5.9.2**: Automated Driver Binary Management
* **Commons IO 2.21.0**: Used for file and screenshot management
* **Maven 3.x**: Dependency and Build Management

### ⚙️ Framework Features

* **PageFactory**: For efficient element initialization and lazy loading   
* **Explicit Waits**: To handle dynamic content and ensure test stability
* **TestNG Listeners**: For enhanced reporting and failure handling
* **Screenshot Capture**: Integrated into the listener for immediate failure analysis

**Note**: This project is a primary component of the Skillo Automation Course portfolio.
