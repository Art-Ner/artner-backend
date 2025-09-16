pipeline {
  agent any
  options { timestamps() }
  triggers { githubPush() }   // GitHub Webhook으로 자동 트리거

  stages {
    stage('Branch check') {
      when { branch 'deploy' }
      steps { echo "deploy 브랜치 트리거 OK" }
    }

    stage('Deploy (docker compose)') {
      when { branch 'deploy' }
      steps {
        sh '''
          set -euxo pipefail
          cd /srv/artner

          echo "[1/2] app 최신 이미지 pull (없으면 무시)"
          docker compose -f /srv/artner/docker-compose.yml pull app || true

          echo "[2/2] app 서비스 재기동"
          docker compose -f /srv/artner/docker-compose.yml up -d app

          echo "[완료] 현재 app 상태:"
          docker compose -f /srv/artner/docker-compose.yml ps app || true
        '''
      }
    }
  }

  post {
    success { echo '배포 파이프라인 완료' }
    failure { echo '배포 파이프라인 실패' }
  }
}