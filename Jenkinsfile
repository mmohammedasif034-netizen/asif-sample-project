pipeline {
  agent any
  options {
    buildDiscarder(logRotator(numToKeepStr: '10'))
    timestamps()
    timeout(time: 30, unit: 'MINUTES')
  }
  environment {
    DOCKER_REGISTRY = 'docker.io'
    IMAGE_NAME = "${DOCKER_REGISTRY}/asiftest123/asif-sample-app"
    IMAGE_TAG = "${BUILD_NUMBER}"
    CONTAINER_NAME = "asif-sample-app"
    CONTAINER_PORT = "8080"
  }
  stages {
    stage('Checkout') {
      steps {
        git url: 'https://github.com/mmohammedasif123/asif-sample-project.git', branch: 'main', credentialsId: 'github-http-credentials'
      }
    }
    stage('Build & Package') {
      steps {
        sh 'mvn -B -DskipTests package'
      }
    }
    stage('Test') {
      steps {
        sh 'mvn -B test'
      }
    }
    stage('Archive Artifacts') {
      steps {
        archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
      }
    }
    stage('Build & Push Docker Image') {
      steps {
        script {
          sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} -t ${IMAGE_NAME}:latest ."
          withCredentials([usernamePassword(credentialsId: 'docker-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
            sh '''
              echo "${DOCKER_PASS}" | docker login -u "${DOCKER_USER}" --password-stdin ${DOCKER_REGISTRY}
              docker push ${IMAGE_NAME}:${IMAGE_TAG}
              docker push ${IMAGE_NAME}:latest
              docker logout
            '''
          }
        }
      }
    }
    stage('Deploy Container') {
      steps {
        script {
          def hostPort = 8000 + (BUILD_NUMBER.toInteger() % 1000)
          sh '''
            docker pull ${IMAGE_NAME}:latest
            docker stop ${CONTAINER_NAME} || true
            docker rm ${CONTAINER_NAME} || true
            docker run -d \
              --name ${CONTAINER_NAME} \
              -p ''' + hostPort + ''':${CONTAINER_PORT} \
              --restart unless-stopped \
              --memory="512m" \
              --cpus="1" \
              -e SPRING_PROFILES_ACTIVE=prod \
              ${IMAGE_NAME}:latest
            sleep 5
            docker ps | grep ${CONTAINER_NAME}
            curl -f http://localhost:''' + hostPort + '''/actuator/health || echo "Health endpoint not available"
          '''
          env.DEPLOYED_PORT = hostPort.toString()
        }
      }
    }
  }
  post {
    success {
      node('built-in') {
        echo "SUCCESS: App deployed at http://localhost:${DEPLOYED_PORT}"
      }
    }
    failure {
      node('built-in') {
        echo 'FAILURE: Pipeline failed'
        sh 'docker ps -a | grep ${CONTAINER_NAME} || true'
      }
    }
    always {
      node('built-in') {
        sh 'docker image prune -f --filter "dangling=true" || true'
      }
    }
  }
}
