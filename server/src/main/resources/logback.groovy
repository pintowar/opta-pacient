import ch.qos.logback.classic.encoder.PatternLayoutEncoder

conversionRule 'clr', org.springframework.boot.logging.logback.ColorConverter
conversionRule 'wex', org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter
conversionRule 'wEx', org.springframework.boot.logging.logback.ExtendedWhitespaceThrowableProxyConverter

logger("org.springframework.web", INFO)

appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = '%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} ' + // Date
                '%clr(%5p) ' + // Log level
                '%clr(%property{PID}){magenta} ' + // PID
                '%clr(---){faint} %clr([%15.15t]){faint} ' + // Thread
                '%clr(%-40.40logger{39}){cyan} %clr(:){faint} ' + // Logger
                '%m%n%wex' // Message
    }
}

root(INFO, ["CONSOLE"])