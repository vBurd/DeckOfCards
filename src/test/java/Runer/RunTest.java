package Runer;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)

@CucumberOptions(
        features = {"src/test/resources/"},
        plugin = {"html:build/cucumber-html-report.html"},
        tags = ("@deckOfCards"),
        glue = {"Glue"})

public class RunTest {
}
