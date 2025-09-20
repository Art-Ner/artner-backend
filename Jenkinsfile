pipeline {
  agent any
  options { timestamps() }
  triggers { githubPush() }   // GitHub Webhook으로 자동 트리거

  environment {
    IMAGE_NAME = "ghcr.io/art-ner/artner-backend"
  }

  stages {
    stage('Branch check') {
      when { branch 'deploy' }
      steps {
        echo "deploy 브랜치 트리거 OK (commit: ${env.GIT_COMMIT})"
      }
    }

    stage('Docker Login (GHCR)') {
      when { branch 'deploy' }
      steps {
        withCredentials([string(credentialsId: 'ghcr_pat', variable: 'GHCR_PAT')]) {
          sh '''#!/bin/bash
            set -euxo pipefail
            echo "$GHCR_PAT" | docker login ghcr.io -u ${GIT_USERNAME:-art-ner} --password-stdin
          '''
        }
      }
    }

    stage('Docker Build & Push') {
      when { branch 'deploy' }
      environment {
        COMMIT = "${env.GIT_COMMIT}"
      }
      steps {
        sh '''#!/bin/bash
          set -euxo pipefail
          docker build -t ${IMAGE_NAME}:latest -t ${IMAGE_NAME}:${COMMIT} .
          docker push ${IMAGE_NAME}:latest
          docker push ${IMAGE_NAME}:${COMMIT}
        '''
      }
    }

    stage('Deploy (docker compose)') {
      when { branch 'deploy' }
      steps {
        sh '''#!/bin/bash
          set -euxo pipefail
          cd /srv/artner

          echo "[1/2] app 최신 이미지 pull"
          docker compose -f docker-compose.yml pull app || true

          echo "[2/2] app 서비스 재기동"
          docker compose -f docker-compose.yml up -d app

          echo "[완료] 현재 app 상태:"
          docker compose -f docker-compose.yml ps app || true
        '''
      }
    }
  }

  post {
    success { echo '배포 파이프라인 완료' }
    failure { echo '배포 파이프라인 실패' }
  }
}