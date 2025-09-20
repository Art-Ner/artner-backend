pipeline {
  agent any
  options { timestamps() }
  triggers { githubPush() }   // GitHub Webhook으로 자동 트리거

  environment {
    IMAGE_NAME = 'ghcr.io/art-ner/artner-backend'
  }

  stages {
    stage('Branch check') {
      when { branch 'deploy' }
      steps { echo "deploy 브랜치 트리거 OK" }
    }

    stage('Docker Login (GHCR)') {
      when { branch 'deploy' }
      steps {
        withCredentials([usernamePassword(credentialsId: 'ghcr_pat', usernameVariable: 'GHCR_USER', passwordVariable: 'GHCR_PAT')]) {
          sh '''
            set -euxo pipefail
            echo "$GHCR_PAT" | docker login ghcr.io -u "$GHCR_USER" --password-stdin
          '''
        }
      }
    }

    stage('Docker Build & Push') {
      when { branch 'deploy' }
      environment { COMMIT = "${env.GIT_COMMIT}" }
      steps {
        sh '''
          set -euxo pipefail
          # 리포 루트(여기에 Dockerfile 있다고 가정)
          docker build -t ${IMAGE_NAME}:latest -t ${IMAGE_NAME}:${COMMIT} .
          docker push  ${IMAGE_NAME}:latest
          docker push  ${IMAGE_NAME}:${COMMIT}
        '''
      }
    }

    stage('Deploy (docker compose)') {
      when { branch 'deploy' }
      steps {
        sh '''
          set -euxo pipefail
          cd /srv/artner

          echo "[1/2] app 최신 이미지 pull"
          docker compose -f /srv/artner/docker-compose.yml pull app || true

          echo "[2/2] app 서비스 재기동(강제 교체)"
          docker compose -f /srv/artner/docker-compose.yml up -d --force-recreate app

          echo "[완료] 현재 app 상태:"
          docker compose -f /srv/artner/docker-compose.yml ps app || true
        '''
      }
    }
  }

  post {
    success { echo '배포 파이프라인 완료' }
    failure { echo '배포 파이프라인 실패' }
    always {
      sh 'docker logout ghcr.io || true'
    }
  }
}