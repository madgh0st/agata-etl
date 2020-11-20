#!/bin/bash

printHelpMessage() {
	echo -e \
"Synopsis:
	$0 [FLAG]
	$0 [FLAG] [FLAG VALUE]

Usage example:
	$0 -s clean-tables --profile dev

Options:
	-s, --step [FLAG VALUE] - start application since specific step. Default: raw-files

	-p, --profile [FLAG VALUE] - set run profile. Default: prod

	-d, --date [FLAG VALUE] - set date of source dir. Default: lates

	-h, --help - shows this message
	"
}

handleInputParams() {
  index=1
  for param in "$@"
  do
    value=$((index+1))
    case $param in
      -s | --step)
        if [ "${!value}" == "raw-files" ] || \
           [ "${!value}" == "json-to-hive" ] || \
           [ "${!value}" == "clean-tables" ] || \
           [ "${!value}" == "available-list-do" ] || \
           [ "${!value}" == "write-eventlog" ] || \
           [ "${!value}" == "validation" ]; then
          step="${!value}"
        else
          echo "Wrong step name"
          exit 1
        fi
      ;;
      -p | --profile)
        if [ "${!value}" == "dev" ] || \
           [ "${!value}" == "prod" ]; then
          profile="${!value}"
        else
          echo "Wrong profile name"
          exit 1
        fi
      ;;
      -h | --help)
        if [ "${!value}" == "dev" ] || \
           [ "${!value}" == "prod" ]; then
          profile="${!value}"
        else
          printHelpMessage
          exit 0
        fi
      ;;
    -d | --date)
        date_download="${!value}"
    ;;
    esac
  ((index++))
  done
}
