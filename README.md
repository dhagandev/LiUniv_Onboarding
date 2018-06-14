# Lithium University Onboarding

## Getting Started ##
TODO: Add set up instructions. (Install Java, IntelliJ, etc)
Include: Reminder to set Java in $PATH

## Running the Lithium University Onboarding Program ##

### Fill in Missing Information ###
1. Open the project in IntelliJ and navigate to the ```hardcoded_keys_template.xml``` file.
2. Copy and paste this file; rename it ```hardcoded_keys.xml```
3. Fill in the fields for the consumerKey, consumerSecret, accessToken, and accessSecret within ```hardcoded_keys.xml```.

### Create the JAR File ###
4. Open a terminal shell and navigate to the LiUni_Onboarding project.
5. Run ```cd src/main/java```.
6. Run ```javac -cp ../../../twitter4j-core-4.0.4.jar LiUni/*.java``` to compile the project.
7. Run ```jar cfm ../../../out/artifacts/LiUni_Onboarding_jar/LiUni_Onboarding.jar META-INF/MANIFEST.MF LiUni/*.class```
8. Move your ```hardcoded_keys.xml``` to ```out/artifacts/LiUni_Onboarding_jar```

### Run the JAR File ###
9. Navigate to ```LiUni_Onboarding.jar```'s location in a terminal shell window.
10. Run the file using ```java -jar LiUni_Onboarding.jar```. Follow the prompts.

## Exit Codes ##
If the program exits prematurely, a status code will be given. The following is a table of what the possible codes are.
Exit code -1: Twitter Exception
Exit code -2: Key Handler

TODO: Test this README on a clean machine.
