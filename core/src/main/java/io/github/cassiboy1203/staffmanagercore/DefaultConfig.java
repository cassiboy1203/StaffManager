package io.github.cassiboy1203.staffmanagercore;

import io.github.cassiboy1203.staffManagerLib.annotations.config.Config;

import java.util.List;

@Config("test.yml")
public class DefaultConfig {
    private List<String> strings;
    private List<ListItem> items;
}
