node {
    def FRONTEND_DIR = 'frontend' // Directory of your frontend application
    def BACKEND_DIR = 'backend'  // Directory of your backend application
    def MAVEN_HOME = '/usr/share/maven' // Path to Maven installation (adjust if necessary)

    try {
        stage('Install Dependencies') {
            parallel(
                'Frontend Dependencies': {
                    dir(FRONTEND_DIR) {
                        sh 'npm install' // Use 'yarn install' if using Yarn
                    }
                },
                'Backend Dependencies': {
                    dir(BACKEND_DIR) {
                        sh "${MAVEN_HOME}/bin/mvn dependency:resolve"
                    }
                }
            )
        }

        stage('Build Applications') {
            parallel(
                'Build Frontend': {
                    dir(FRONTEND_DIR) {
                        sh 'npm run build' // Use 'ng build --prod' if Angular CLI
                    }
                },
                'Build Backend': {
                    dir(BACKEND_DIR) {
                        sh "${MAVEN_HOME}/bin/mvn clean package"
                    }
                }
            )
        }

        stage('Archive Artifacts') {
            archiveArtifacts artifacts: "${BACKEND_DIR}/target/*.jar", fingerprint: true
            archiveArtifacts artifacts: "${FRONTEND_DIR}/dist/**", fingerprint: true
        }

        stage('Deploy (Optional)') {
            echo 'Deployment stage would go here.'
            // Add deployment steps here if needed
        }

    } catch (Exception e) {
        echo "Build failed: ${e.message}"
        throw e
    } finally {
        echo 'Cleaning up workspace...'
        cleanWs() // Cleans up the workspace
    }
}
