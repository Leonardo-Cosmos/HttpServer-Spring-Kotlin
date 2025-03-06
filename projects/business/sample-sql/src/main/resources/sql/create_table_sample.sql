CREATE TABLE `sample_text` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `key` VARCHAR(128),
  `text` VARCHAR(4096),
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  UNIQUE INDEX `key_UNIQUE` (`key` ASC) VISIBLE);