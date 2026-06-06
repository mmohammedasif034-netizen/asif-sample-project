pipeline {
  agent any
  options {
    buildDiscarder(logRotator(numToKeepStr: '10'))
    timestamps()
    timeout(time: 30, unit: 'MINUTES')
  }
  stages {
    stage('Prepare') {
      steps {
        script {
          echo 'Preparing workspace (clean)'
          deleteDir()
        }
      }
    }
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
          echo 'Stopping any existing app process...'
          pkill -f /tmp/mainframe-ui.jar || true
          echo 'Copying jar to /tmp and starting UI on port 8081...'
          cp target/*.jar /tmp/mainframe-ui.jar
          nohup java -jar /tmp/mainframe-ui.jar --server.port=8081 > /tmp/app.log 2>&1 &
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
          # copy log back to workspace for archiving
          cp /tmp/app.log ./app.log || true
        '''
      }
    }
  }
  post {
    success { echo 'SUCCESS: Pipeline completed' }
    failure { echo 'FAILURE: Pipeline failed' }
  }
}
