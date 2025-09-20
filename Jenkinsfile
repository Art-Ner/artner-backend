pipeline {
  agent any
  options { timestamps() }
  triggers { githubPush() }

  environment {
    IMAGE_NAME = "ghcr.io/art-ner/artner-backend"
    COMMIT     = "${env.GIT_COMMIT}"
  }

  stages {
    stage('Branch check') {
      when { expression { return env.BRANCH_NAME == 'deploy' || env.GIT_BRANCH == 'origin/deploy' } }
      steps {
        echo "deploy 브랜치 트리거 OK (commit: ${COMMIT})"
      }
    }

    stage('Docker Login (GHCR)') {
      when { expression { return env.BRANCH_NAME == 'deploy' || env.GIT_BRANCH == 'origin/deploy' } }
      steps {
        withCredentials([usernamePassword(credentialsId: 'ghcr_pat', usernameVariable: 'GHCR_USER', passwordVariable: 'GHCR_PAT')]) {
          sh '''#!/bin/bash
            set -euxo pipefail
            echo "$GHCR_PAT" | docker login ghcr.io -u "$GHCR_USER" --password-stdin
          '''
        }
      }
    }

    stage('Docker Build & Push') {
      when { expression { return env.BRANCH_NAME == 'deploy' || env.GIT_BRANCH == 'origin/deploy' } }
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
      when { expression { return env.BRANCH_NAME == 'deploy' || env.GIT_BRANCH == 'origin/deploy' } }
      steps {
        sh '''#!/bin/bash
          set -euxo pipefail
          cd /srv/artner
          echo "[1/2] 최신 이미지 pull"
          docker compose -f docker-compose.yml pull app || true
          echo "[2/2] app 서비스 재기동"
          docker compose -f docker-compose.yml up -d app
          docker compose -f docker-compose.yml ps app || true
        '''
      }
    }
  }

  post {
    always  { sh 'docker logout ghcr.io || true' }
    success { echo ' 배포 파이프라인 완료' }
    failure { echo ' 배포 파이프라인 실패' }
  }
}