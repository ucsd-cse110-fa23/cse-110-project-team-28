# CSE 110 Team 28 PantryPal Project

[![Java CI with Gradle](https://github.com/ucsd-cse110-fa23/cse-110-project-team-28/actions/workflows/gradle.yml/badge.svg)](https://github.com/ucsd-cse110-fa23/cse-110-project-team-28/actions/workflows/gradle.yml)

## Running the App

To run the app, run the following command in the root directory of the project:

```bash
./gradlew run
```

## Running the Tests

To run the tests, run the following command in the root directory of the project:

```bash
./gradlew test
```

<!-- tree -I "build|.gradle|.vscode|bin|gradle" -->

## Project Architecture

Our app follows the Model-View-Controller (MVC) architecture.

### Models

The model is responsible for storing the data of the app. The model is implemented in the `model` package. The model consists of two classes: `Recipe` and `RecipeData`.

The `Recipe` class represents a single recipe. It stores the name of the recipe, the ingredients, the instructions, and the meal type. It also provides methods for getting and setting these values.

The `RecipeData` class represents the data of the app. It stores a list of recipes. It also provides methods for getting and setting the list of recipes.

### Views

The view is responsible for displaying the data to the user. The views are implemented as FXML files in the `resources/fxml` directory. Each view has a corresponding controller in the `controller` package.

Read the [Editing & Creating Views](#editing--creating-views) section for information on how to edit or create views.

### Controllers

The controller is responsible for handling user input and updating the model and view accordingly. The controllers are implemented in the `controller` package. Each controller has a corresponding FXML file in the `resources/fxml` directory.

## Editing & Creating Views

Edit or create views by editing or creating FXML files in the `resources/fxml` directory. Each FXML file must have a corresponding controller in the `controller` package.

For consistency, the controller must have the same name as the FXML file, but with the word `Controller` appended to the end. For example, the controller for `main.fxml` is `MainController.java`.

## Scene Builder

Use [Scene Builder](https://gluonhq.com/products/scene-builder/) to edit FXML files. Scene Builder is a drag-and-drop tool for editing FXML files. It allows you to edit the FXML file visually, without having to write the XML code yourself.

It is recommended that you edit FXML files in Scene Builder rather than editin them manually. Beware that changes made to FXML files manually may be overwritten by Scene Builder.

#### Adding Controllers to FXML Files

You can associate a controller with an FXML file in Scene Builder by clicking on the "Controller" tab under the "Document" section in the bottom left, then entering the fully qualified name of the controller class in the "Controller Class" field.

#### Adding IDs to Nodes

You can add `fx:id` attributes to nodes in Scene Builder by clicking on the "Code" tab under the "Inspector" section on the right, then entering the ID in the "fx:id" field.

Note that the ID cannot contain spaces or special characters. It must start with a letter or underscore, and it must be a valid Java identifier. For example, `myButton` is a valid ID, but `my-button` is not.

#### Referencing Nodes in Controllers

You can reference nodes in the controller by adding `@FXML` annotations to fields in the controller. The name of the field must match the `fx:id` of the node. For example, if you have a `Button` with `fx:id="myButton"`, you can reference it in the controller with the following code:

```java
@FXML
private Button myButton;
```

#### Switching Between Views

Below is an example from `EditRecipeController.java` that switches to the `main.fxml` view:

```java
@FXML
private TextArea editRecipeTextArea;   

// ...

private void goHome() throws IOException {
    Scene scene = editRecipeTextArea.getScene();
    Stage stage = (Stage) scene.getWindow();
    Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
    Scene newScene = new Scene(root, scene.getWidth(), scene.getHeight());

    stage.setScene(newScene);
    stage.show();
}
```

Here, `editRecipeTextArea` is a `TextArea` node in the `EditRecipeController` view. `main.fxml` is the FXML file for the `MainController` view.

## Gradle

Gradle is a build automation tool used to compile, test, and run our code. You can invoke Gradle using the `gradlew` script in the root directory of the project. This script will download the correct version of Gradle for you, so you don't need to install Gradle on your machine.

Alternatively, you can install Gradle on your machine and invoke it using the `gradle` command. Please note that this may cause issues if you are using a different version of Gradle than the one specified in `gradle/wrapper/gradle-wrapper.properties`.

## GitHub Actions

The configuration for GitHub Actions is stored in `.github/workflows/gradle.yml`. This workflow is triggered on every push to main and every pull request to main. The workflow builds the project with Gradle and runs the tests. If either of these steps fail, the workflow will fail and the push/pull request will be rejected.
