def FRONTEND_DIR = 'frontend'
def BACKEND_DIR = 'backend'
def MAVEN_HOME = '/usr/share/maven'

def runCommand(String command, String dir = '.') {
    println "Running: ${command} in ${dir}"
    def proc = ["bash", "-c", "cd ${dir} && ${command}"].execute()
    proc.in.eachLine { line -> println line }
    proc.err.eachLine { line -> System.err.println line }
    proc.waitFor()
    if (proc.exitValue() != 0) {
        throw new RuntimeException("Command failed: ${command} in ${dir}")
    }
}

// Wrap all steps in try-catch-finally
try {
    println "Installing frontend dependencies..."
    runCommand('npm install', FRONTEND_DIR)

    println "Installing backend dependencies..."
    runCommand("${MAVEN_HOME}/bin/mvn dependency:resolve", BACKEND_DIR)

    println "Building frontend..."
    runCommand('npm run build', FRONTEND_DIR)

    println "Building backend..."
    runCommand("${MAVEN_HOME}/bin/mvn clean package", BACKEND_DIR)

    println "Archiving artifacts..."
    // Archiving logic depends on your environment
    // Here just print paths that you would archive
    println "Archive backend jars from: ${BACKEND_DIR}/target/*.jar"
    println "Archive frontend dist files from: ${FRONTEND_DIR}/dist/"

    println "Deployment stage (optional)..."
    // Add deployment commands here if needed

} catch (Exception e) {
    println "Build failed: ${e.message}"
    System.exit(1)
} finally {
    println "Cleaning up workspace..."
    // Add any cleanup commands here, like deleting temporary files if needed
}
