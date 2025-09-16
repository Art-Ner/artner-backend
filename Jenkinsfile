pipeline {
  agent any
  options { timestamps() }
  triggers { githubPush() }   // GitHub Webhook

  stages {
    stage('Branch check') {
      when { branch 'deploy' }
      steps { echo "deploy 브랜치 트리거 OK" }
    }

    stage('Deploy (docker compose)') {
      when { branch 'deploy' }
      steps {
        sh '''
          set -euo pipefail
          cd /srv/artner

          # 최신 이미지가 있는 경우만 가져오고(없어도 통과), app만 재기동
          docker compose pull app || true
          docker compose up -d app

          # 상태 확인(로그에 찍기)
          docker compose ps app || true
        '''
      }
    }
  }

  post {
    success { echo '배포 완료' }
    failure { echo '배포 실패' }
  }
}