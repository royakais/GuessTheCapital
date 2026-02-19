Since you have a finished, polished game, your **README.md** should look professional and highlight the specific features we built together, like the "Double-Enter" logic and the "Shake" animation.

Copy and paste the block below into a file named `README.md` in your project folder.

---

# 🌍 Guess the Capital with Roya

A sleek, fast-paced Java Swing desktop application designed to test your geography knowledge. Featuring 96 countries, a high-stakes timer, and a focus on seamless keyboard-driven gameplay.

## ✨ Features

* **96 Country Database:** Challenges covering every corner of the globe.
* **Smart "Double-Enter" UX:** * **1st Enter:** Submits your answer.
* **2nd Enter:** Moves to the next country.
* Designed so your hands never have to leave the keyboard.


* **Dynamic Timer:** 15 seconds per question with a color-changing progress bar (Green → Orange → Red).
* **Interactive Animations:**
* **Shake Effect:** The window physically shakes when you get an answer wrong.
* **Emoji Feedback:** Random celebratory or "thinking" emojis based on your performance.


* **Modern Dark Theme:** A clean, focused interface using `CardLayout` for smooth screen transitions.
* **Lenient Matching:** Automatically ignores extra periods (e.g., "Washington D.C." and "Washington DC" are both correct).

## 🛠️ Technical Overview

* **Language:** Java
* **Framework:** Swing & AWT
* **Architecture:** `CardLayout` for state management (Welcome vs. Game screen).
* **Logic:** `HashMap` for  capital lookups and `Timer`-based programmatic animations.

## 🚀 Getting Started

### Prerequisites

* Java Development Kit (JDK) 8 or higher.

### Installation & Execution

1. **Clone the repository:**
```bash
git clone https://github.com/YourUsername/Guess-the-Capital-with-Roya.git

```


2. **Navigate to the folder:**
```bash
cd Guess-the-Capital-with-Roya

```


3. **Compile the code:**
```bash
javac Main.java Game.java

```


4. **Run the game:**
```bash
java Main

```



## 🎮 How to Play

1. Enter your name on the **Welcome Screen** and press **Enter**.
2. The game will display a country. Type the capital in the box.
3. Press **Enter** to check your answer.
4. If the timer hits zero or you get it wrong, the window will **shake** and reveal the correct answer.
5. Press **Enter** again to go to the next question!
6. <img width="730" height="894" alt="image" src="https://github.com/user-attachments/assets/d793eabb-a8f2-42b3-850a-d46227a0cf8f" />


---
