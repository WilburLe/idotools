package com.toolbox.weather.enums;
   /**
     * 空气质量等级
     * @return
     *
     */
    public enum PM25Level {
        LevelOne(1, "优", 0, 50, "优"), //
        LevelTwo(2, "良", 51, 100, "良"), //
        LevelThree(3, "轻度污染", 101, 150, "轻度污染"), //
        LevelFour(4, "中度污染", 151, 200, "中度污染"), //
        LevelFive(5, "重度污染", 201, 300, "重度污染"), //
        LevelSix(6, "严重污染", 300, Integer.MAX_VALUE, "严重污染");//

        int    level;
        String label;
        String suggest;
        int    minValue;
        int    maxValue;

        PM25Level(int level, String label, int minValue, int maxValue, String suggest) {
            this.level = level;
            this.label = label;
            this.minValue = minValue;
            this.maxValue = maxValue;
            this.suggest = suggest;
        }

        public String toString() {
            return label;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public int getMinValue() {
            return minValue;
        }

        public void setMinValue(int minValue) {
            this.minValue = minValue;
        }

        public int getMaxValue() {
            return maxValue;
        }

        public void setMaxValue(int maxValue) {
            this.maxValue = maxValue;
        }

        public String getSuggest() {
            return suggest;
        }

        public void setSuggest(String suggest) {
            this.suggest = suggest;
        }
    }