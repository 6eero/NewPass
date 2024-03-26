<div align="center">
    <img width="200" height="200" style="display: block; border: 1px solid #f5f5f5; border-radius: 9999px;" src="https://github.com/6eero/NewPass/assets/114809573/77aeeea8-5440-433b-8621-2a5b54173896">
</div>

<div align="center">
    <h1>NewPass</h1>
</div>

<div align="center">
    <img alt="GitHub" src="https://img.shields.io/github/license/Ashinch/ReadYou?color=D0BCFF&style=flat-square">
    <a target="_blank" href="https://github.com/6eero/NewPass/releases">
        <img alt="Version" src="https://img.shields.io/github/v/release/6eero/NewPass?color=D0BCFF&label=version&style=flat-square">
    </a>
    <img alt="GitHub last commit" src="https://img.shields.io/github/last-commit/6eero/NewPass?color=D0BCFF&style=flat-square">
</div>

<br>

<p align="right">
   <img src="https://github.com/6eero/NewPass/assets/114809573/da89a98d-585b-443f-a2ee-6fbb592fbad5" title="UI">
</p>

<br>

# 📍Intro
NewPass is a secure password management application designed to generate and store strong passwords locally on your device. With NewPass, you can create highly secure passwords for your accounts and services without the need to remember them.


## 🗝️ Key Features:
- **Password Generation**: NewPass provides a robust password generator that allows you to create complex and secure passwords tailored to your specific requirements. You can customize the length and the character set (Uppercase, Numbers and Special).

- **Local Storage**: Your passwords are stored locally on your device, ensuring complete privacy and control over your data. NewPass does not store any passwords on external servers, minimizing the risk of unauthorized access (If you uninstall the app, your password are lost!).

- **AES Encryption**: NewPass encrypts all stored passwords using Advanced Encryption Standard (AES) with Cipher Block Chaining (CBC) mode before saving them in the local database.

- **SQLite Chiper**: NewPass utilizes SQLCipher, an extension for SQLite databases, to bolster security further by encrypting entirely the database, ensuring robust protection against unauthorized access. The encryption key is chosen by the user upon the first launch of the app, and it remains saved and encrypted in an EncryptedSharedPreferences. It is then requested every time the app is launched. 

- **User-Friendly Interface**: NewPass features an intuitive and user-friendly interface, making it easy to generate, view, and manage your passwords. The app offers convenient options for copying passwords to the clipboard and securely sharing them with other applications.


## ⬇️ Download 
[<img src="https://s1.ax1x.com/2023/01/12/pSu1a36.png" alt="Get it on GitHub" height="80">](https://github.com/6eero/NewPass/releases)


## 🧱 Build
1. First you need to get the source code of NewPaass.
```
git clone https://github.com/6eero/NewPass.git
```
2. Open the project in [Android Studio](https://developer.android.com/studio).
3. When you click the `▶ Run` button, it will be built automatically.
4. Launch NewPass.
  

## ⚒️ Todo
- [ ] Prevent adding duplicate entries to the database.
- [ ] Fix small screen layout display issue.
- [x] Fix random crashes with multiple users.
- [x] Improve the login GUI.
- [x] Add a login menu to unlock the application.
- [x] Currently the encryption key and IV vector are hardcoded into the source code. I have to find a way to mask them.
- [x] Add encryption: The app does not currently provide secrecy restrictions on entered passwords.

## 📜 License
GNU GPL v3.0 © [NewPass](https://github.com/6eero/NewPass/blob/master/LICENSE)
