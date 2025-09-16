pipeline {
  agent any
  options { timestamps() }
  triggers { githubPush() }  // GitHub Webhook과 연결

  stages {
    stage('Branch check') {
      when { branch 'deploy' }
      steps {
        echo "deploy 브랜치 트리거 OK"
      }
    }

    stage('Sanity') {
      when { branch 'deploy' }
      steps {
        sh '''
          set -euxo pipefail
          echo "Jenkins 워크스페이스 확인:"
          pwd; ls -la
        '''
      }
    }
  }

  post {
    success { echo ' 파이프라인 성공' }
    failure { echo ' 파이프라인 실패' }
  }
}