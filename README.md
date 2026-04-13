# Skillo Selenium Automation Project

This repository contains a UI automation framework for the **Skillo** social media platform. The project is built using
**Java 17**, **Selenium WebDriver**, and **TestNG**, implementing the **Page Object Model (POM)** design pattern.

---

## 📂 Project Structure

```text
SkilloJavaSeleniumProject/
├── pom.xml                      # Maven configuration and dependencies
├── testng.xml                   # Test suite configuration and listeners
└── src/
    ├── main/java/skillo.automation/
    │   ├── BasePage.java        # Shared utilities (waits, navigation, common actions)
    │   ├── LoginPage.java       # Login and authentication page objects
    │   ├── HomePage.java        # Feed navigation and like-related logic
    │   └── ProfilePage.java     # User profile, post creation, and post deletion logic
    ├── test/resources/
    │   ├── screenshots/         # Directory for failure screenshots (auto-generated)
    │   └── laleta.jpg           # Sample asset for post upload tests
    └── test/java/skillo.automation/
        ├── listeners/
        │   └── ScreenshotListener.java # Failure screenshot capture logic
        └── tests/
            ├── BaseTest.java    # Browser lifecycle and suite-level setup
            ├── LoginTests.java  # Login, invalid login, navigation, and logout tests
            ├── HomeTests.java   # Like persistence verification after refresh
            ├── PostTests.java   # Post creation and cleanup
            └── ProfileTests.java # Profile navigation and post count validation
```

---

# 🏗️ Architecture Overview

### **Page Object Model (POM)**

The framework maintains a strict separation between **Test Logic** (the *"What"*) and **Page Locators/Interactions** (the *"How"*). This ensures that if the UI changes, updates are only needed in one place, making the suite highly maintainable.

### **Reporting & Failure Analysis**

* **📸 Screenshot Listener:** A custom TestNG listener automatically captures the browser state at the exact moment of any test failure.
* **🧹 Screenshot Cleanup:** The screenshots folder is cleaned before the suite starts so that only screenshots from the current run remain.
* **📜 Detailed Logging:** The framework provides clear console output for every execution step (e.g. `Step: Entering credentials...`), facilitating easy debugging.

---

# 🧪 Key Test Scenarios

* **🔐 Auth Security (Login):** Covers successful login, invalid password handling, navigation links visibility, and logout.
* **🖼️ Post Lifecycle (Post):** Automates post creation with image upload and caption submission, followed by automatic cleanup after the test.
* **📊 Profile Count Validation (Profile):** Verifies that the profile header count and visible gallery count increase after creating a new post.
* **❤️ Like Persistence (Home):** Verifies that the like count changes in the modal and persists correctly in the feed after a page refresh.

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

### **Analyzing Results**

* **Console Output:** Review the logs for DEBUG, SUCCESS, or ERROR messages.
* **Visual Evidence:** Check the screenshots folder for images captured during failed assertions.

---

### 🛠️ Tech Stack

* **Java 17**: Programming Language
* **Selenium WebDriver 4.41.0**: Browser Automation Engine
* **TestNG 7.12.0**: Test Runner and Assertions
* **Maven 3.x**: Dependency and Build Management

### ⚙️ Framework Features

* **PageFactory**: For efficient element initialization and lazy loading
* **Explicit Waits**: To handle dynamic content and improve test stability
* **Reusable Page Methods**: Common actions such as click, type, and post creation are centralized in page objects
* **TestNG Listeners**: For enhanced reporting and failure handling
* **Screenshot Capture**: Integrated into the listener for immediate failure analysis

**Note**: This project is a primary component of the Skillo Automation Course portfolio.
