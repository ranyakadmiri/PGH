def DOCKER_HUB_USERNAME = 'ranya631'
def DOCKER_HUB_PASSWORD = '123456789' // Consider using environment variable for security
def BACKEND_IMAGE_NAME = 'nomprenomlasseexamen-backend'
def FRONTEND_IMAGE_NAME = 'nomprenomlasseexamen-frontend'
def BACKEND_IMAGE_TAG = "${DOCKER_HUB_USERNAME}/${BACKEND_IMAGE_NAME}:latest"
def FRONTEND_IMAGE_TAG = "${DOCKER_HUB_USERNAME}/${FRONTEND_IMAGE_NAME}:latest"
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
    // Frontend build
    println "Installing frontend dependencies..."
    runCommand('npm install', FRONTEND_DIR)
    
    println "Building frontend..."
    runCommand('npm run build', FRONTEND_DIR)
    
    println "Building frontend Docker image..."
    runCommand("docker build -t ${FRONTEND_IMAGE_NAME} .", FRONTEND_DIR)
    
    println "Tagging frontend image for Docker Hub..."
    runCommand("docker tag ${FRONTEND_IMAGE_NAME} ${FRONTEND_IMAGE_TAG}")
    
    // Backend build
    println "Building backend Docker image (Java 17)..."
    runCommand("docker build -t ${BACKEND_IMAGE_NAME} .", BACKEND_DIR)
    
    // Extract JAR from backend container
    runCommand('docker rm -f temp-extract || true', BACKEND_DIR)
    runCommand("docker create --name temp-extract ${BACKEND_IMAGE_NAME}", BACKEND_DIR)
    runCommand('docker cp temp-extract:/app/app.jar ./target/', BACKEND_DIR)
    runCommand('docker rm temp-extract', BACKEND_DIR)
    
    println "Tagging backend image for Docker Hub..."
    runCommand("docker tag ${BACKEND_IMAGE_NAME} ${BACKEND_IMAGE_TAG}")
    
    // Login to Docker Hub
    println "Logging in to Docker Hub..."
    runCommand("docker login -u ${DOCKER_HUB_USERNAME} -p ${DOCKER_HUB_PASSWORD}")
    
    // Push images to Docker Hub
    println "Pushing frontend image to Docker Hub..."
    runCommand("docker push ${FRONTEND_IMAGE_TAG}")
    
    println "Pushing backend image to Docker Hub..."
    runCommand("docker push ${BACKEND_IMAGE_TAG}")
    
    println "Build and push completed successfully!"
    println "Backend JAR: ${BACKEND_DIR}/target/*.jar"
    println "Frontend dist: ${FRONTEND_DIR}/dist/"
    println "Images pushed to Docker Hub:"
    println "- ${FRONTEND_IMAGE_TAG}"
    println "- ${BACKEND_IMAGE_TAG}"
    
} catch (Exception e) {
    println "Build failed: ${e.message}"
    System.exit(1)
} finally {
    println "Cleaning up..."
}
