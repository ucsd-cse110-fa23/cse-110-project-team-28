import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.*;
import java.util.ArrayList;

import org.bson.Document;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import controller.MainController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Recipe;
import model.UserData;
import utilites.MongoDBHelper;
import utilites.RecipeHelper;

@RunWith(MockitoJUnitRunner.class)
public class SavingLoadingTests extends ApplicationTest {
    @Mock
    private MongoClient mockMongoClient;

    @Mock
    private MongoDatabase mockDatabase;

    @Mock
    private MongoCollection<Document> mockCollection;

    @Test
    public void testSaveAndLoadRecipe() {
        Recipe testRecipe = new Recipe();
        testRecipe.setName("Test Recipe");

        String testUserId = "testUser123";

        when(mockMongoClient.getDatabase(anyString())).thenReturn(mockDatabase);
        when(mockDatabase.getCollection(anyString())).thenReturn(mockCollection);

        // Test saving the recipe
        MongoDBHelper.insertUserRecipeId(testRecipe.getName(), testUserId);

        // Verify that the recipe was saved
        verify(mockCollection, times(1)).insertOne(any(Document.class));

        // Test loading the recipe
        when(mockCollection.find(any(Document.class)).first()).thenReturn(new Document());
        // Convert the document back to Recipe object as per your implementation in MongoDBHelper

        Recipe loadedRecipe = MongoDBHelper.findRecipeById(testRecipe.getId());

        // Assert that the loaded recipe matches the saved one
        assertEquals("Test Recipe", loadedRecipe.getName());
    }
}

