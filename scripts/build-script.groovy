def FRONTEND_DIR = 'frontend'
def BACKEND_DIR = 'backend'

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

try {
    println "Installing frontend dependencies..."
    runCommand('npm install', FRONTEND_DIR)
    
    println "Building frontend..."
    runCommand('npm run build', FRONTEND_DIR)
    
    println "Building backend with Docker (Java 17)..."
    runCommand(' docker build -t nomprenomlasseexamen-backend .', BACKEND_DIR)
    
    // Extract JAR from container
    //remove existing container 
    runCommand('docker rm -f temp-extract || true', BACKEND_DIR)
    runCommand('docker create --name temp-extract nomprenomlasseexamen-backend', BACKEND_DIR)
    runCommand('docker cp temp-extract:/app/app.jar ./target/', BACKEND_DIR)

   runCommand('docker rm temp-extract', BACKEND_DIR)
    
    println "Build completed successfully!"
    println "Backend JAR: ${BACKEND_DIR}/target/*.jar"
    println "Frontend dist: ${FRONTEND_DIR}/dist/"
    
} catch (Exception e) {
    println "Build failed: ${e.message}"
    System.exit(1)
} finally {
    println "Cleaning up..."
}
