pipeline {
    agent any
    tools {
        maven 'mavendev'
    }
    stages {
        stage('code checkout') {
            steps {
                git branch: 'development', credentialsId: 'github', url: 'https://github.com/mohammadaszadali/aggregation-business-service.git'
            }
        }
stage('Build code')
{
steps{
sh 'mvn clean install'
}
}
    }
}

