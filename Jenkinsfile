pipeline {
  agent any
  options {
    buildDiscarder(logRotator(numToKeepStr: '10'))
    timestamps()
    timeout(time: 30, unit: 'MINUTES')
  }
  stages {
    stage('Checkout') {
      steps {
        git url: 'https://github.com/mmohammedasif034-netizen/asif-sample-project.git', branch: 'main', credentialsId: 'github-http-credentials'
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

    stage('Run UI (agent)') {
      steps {
        sh '''
          echo 'Starting UI on agent...'
          nohup java -jar target/*.jar > app.log 2>&1 &
          # simple health check (tries a few times)
          for i in 1 2 3 4 5; do
            sleep 2
            if curl -sS http://localhost:8080/api/status >/dev/null 2>&1; then
              echo 'UI is responding'
              break
            fi
            echo 'Waiting for UI...'
          done
          curl -sS http://localhost:8080/api/status || true
        '''
      }
    }
  }
  post {
    success { echo 'SUCCESS: Pipeline completed' }
    failure { echo 'FAILURE: Pipeline failed' }
    always { cleanWs() }
  }
}
