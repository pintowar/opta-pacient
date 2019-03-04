appender("CONSOLE", ConsoleAppender) {
    withJansi = true
    encoder(PatternLayoutEncoder) {
        pattern = '%d{yyyy-MM-dd HH:mm:ss.SSS} ' + // Date
                '%highlight(%-5level) ' + // Log level
                '--- ' +
                '[%15.15t] ' + // Thread
                '%boldCyan(%-40.40logger{39}) ' + // Logger
                ': %m%n' // Message
    }
}

root(INFO, ["CONSOLE"])