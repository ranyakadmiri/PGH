def FRONTEND_DIR = 'frontend'
def BACKEND_DIR = 'backend'
def MAVEN_HOME = '/usr/share/maven'
def NPM_PATH = '/home/devops/.nvm/versions/node/v22.17.0/bin'  // Your npm path

def runCommand(String command, String dir = '.', Map env = [:]) {
    println "Running: ${command} in ${dir}"
    def pb = new ProcessBuilder('bash', '-c', "cd ${dir} && ${command}")
    if (!env.isEmpty()) {
        Map<String, String> processEnv = pb.environment()
        env.each { k, v -> processEnv.put(k, v) }
    }
    def proc = pb.start()
    proc.inputStream.eachLine { line -> println line }
    proc.errorStream.eachLine { line -> System.err.println line }
    proc.waitFor()
    if (proc.exitValue() != 0) {
        throw new RuntimeException("Command failed: ${command} in ${dir}")
    }
}

try {
    println "Installing frontend dependencies..."
    runCommand('npm install', FRONTEND_DIR, [PATH: "${NPM_PATH}:" + System.getenv('PATH')])

    println "Installing backend dependencies..."
    runCommand("${MAVEN_HOME}/bin/mvn dependency:resolve", BACKEND_DIR)

    println "Building frontend..."
    runCommand('npm run build', FRONTEND_DIR, [PATH: "${NPM_PATH}:" + System.getenv('PATH')])

    println "Building backend..."
    runCommand("${MAVEN_HOME}/bin/mvn clean package", BACKEND_DIR)

    println "Archiving artifacts..."
    println "Archive backend jars from: ${BACKEND_DIR}/target/*.jar"
    println "Archive frontend dist files from: ${FRONTEND_DIR}/dist/"

    println "Deployment stage (optional)..."
    // Add deployment commands here if needed

} catch (Exception e) {
    println "Build failed: ${e.message}"
    System.exit(1)
} finally {
    println "Cleaning up workspace..."
    // Add any cleanup commands here
}
