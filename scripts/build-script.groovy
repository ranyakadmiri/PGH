def NEXUS_REGISTRY = '192.168.220.151:5000'
def IMAGE_NAME = 'nomprenomlasseexamen-backend'
def IMAGE_TAG = "${NEXUS_REGISTRY}/${IMAGE_NAME}:latest"

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
    runCommand("docker build -t ${IMAGE_NAME} .", BACKEND_DIR)
    
    // Extract JAR from container
    runCommand('docker rm -f temp-extract || true', BACKEND_DIR)
    runCommand("docker create --name temp-extract ${IMAGE_NAME}", BACKEND_DIR)
    runCommand('docker cp temp-extract:/app/app.jar ./target/', BACKEND_DIR)
    runCommand('docker rm temp-extract', BACKEND_DIR)
    
    // Tag the image with Nexus repo URL
    println "Tagging backend image for Nexus registry..."
    runCommand("docker tag ${IMAGE_NAME} ${IMAGE_TAG}")
    
    // Login to Nexus Docker registry (optional if no auth, else provide credentials)
    println "Logging in to Nexus registry..."
    runCommand("docker login ${NEXUS_REGISTRY} -u admin -p admin")
    
    // Push the image to Nexus
    println "Pushing backend image to Nexus registry..."
    runCommand("docker push ${IMAGE_TAG}")
    
    println "Build and push completed successfully!"
    println "Backend JAR: ${BACKEND_DIR}/target/*.jar"
    println "Frontend dist: ${FRONTEND_DIR}/dist/"
    
} catch (Exception e) {
    println "Build failed: ${e.message}"
    System.exit(1)
} finally {
    println "Cleaning up..."
}
