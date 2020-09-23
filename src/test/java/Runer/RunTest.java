package Runer;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)

@CucumberOptions(
        features = {"src/test/resources/"},
        plugin = {"html:build/cucumber-html-report.html"},
        glue = {"Glue"})

public class RunTest {
}
