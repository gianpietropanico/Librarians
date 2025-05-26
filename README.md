![title](https://github.com/AntonioTagliafierro/TheLibrariansApp/blob/master/assets/titolo.jpg)

# Installation
Server
1. In a Linux environment, open the server folder and open the terminal.
2. To give permissions to init.sql with the chmod 664 command, use:
    ```bash
    chmod 664 init.sql
    ```
3. To build the image, run:
   ```bash
    docker compose build
    ```
4. To run the containers again, execute:
   
   ```bash
    docker compose up
    ```

Client
1. Clone the repository:
    ```bash
    git clone https://github.com/AntonioTagliafierro/TheLibrariansApp.git
    ```
2. Open the project in Android Studio:
    - Launch Android Studio.
    - Select "Open an existing Android Studio project."
    - Navigate to the folder where you cloned the repository and select the `build.gradle` file in the `app` folder.
3. Set up the virtual device or connect a physical device.

# Run
1. Launch the application from Android Studio.
2. Select the virtual or physical device on which you want to run the application.
3. Click on "Run" in Android Studio.
