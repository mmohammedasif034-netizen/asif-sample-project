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
          echo 'Starting UI on agent (port 8081)...'
          nohup java -jar target/*.jar --server.port=8081 > app.log 2>&1 &
          # simple health check (tries a few times)
          for i in 1 2 3 4 5; do
            sleep 2
            if curl -sS http://localhost:8081/api/status >/dev/null 2>&1; then
              echo 'UI is responding on 8081'
              break
            fi
            echo 'Waiting for UI on 8081...'
          done
          curl -sS http://localhost:8081/api/status || true
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
