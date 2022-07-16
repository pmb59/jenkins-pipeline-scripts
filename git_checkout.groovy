nnode('lsf-submitter') {

  stage('git version') {
    sh 'git --version'
  }

  stage('checkout github') {

    // checkout github repo
    checkout(changelog: false,
      poll: false,
      scm: [$class: 'GitSCM',
        branches: [
          [name: '*/master']
        ],
        extensions: [
          [$class: 'SubmoduleOption',
            depth: 1,
            disableSubmodules: false,
            parentCredentials: false,
            recursiveSubmodules: false,
            reference: '',
            shallow: true,
            trackingSubmodules: false
          ]
        ],
        gitTool: 'Default',
        userRemoteConfigs: [
          [url: 'https://github.com/pmb59/bioconductor-ci-validator']
        ]
      ])

    sh '''#!/bin/bash
      echo $WORKSPACE
      mkdir subdir_repo
    '''
  }

  // checkout gitlab private repo in a subdirectory
  stage('checkout gitlab') {

    dir('subdir_repo') {

      checkout(changelog: false,
        poll: false,
        scm: [
          $class: 'GitSCM',
          branches: [
            [name: '*/master']
          ],
          extensions: [],
          userRemoteConfigs: [
            [
              credentialsId: '<CREDENTIALS>',
              url: 'ssh://git@gitlab.<REPO>.git'
            ]
          ]
        ])
    }
  }
}
