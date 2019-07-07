#!/bin/bash
set -e
EXIT_STATUS=0

./gradlew check --no-daemon || EXIT_STATUS=$?

exit $EXIT_STATUS
