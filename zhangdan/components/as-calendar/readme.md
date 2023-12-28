# 调用示例

```vue
<view class="content">
    <as-calendar ref='as' :markDays='markDays' :headerBar='false' @onDayClick='onDayClick'></as-calendar>
    <view class="change">选中日期：{{curDate}}</view>
</view>
```

```vue
 import asCalendar from '@/components/as-calendar/as-calendar.vue'
 export default {
  components:{
    asCalendar
  },
  data() {
   return {
      curDate:'',
      markDays:[]
   }
  },
  onReady() {
            let today = this.$refs.as.getToday().date;
            this.curDate = today;
            this.markDays.push(today);
  },
  methods: {
            onDayClick(data){
                this.curDate = data.date;
            }
  }
 }
```

## 属性说明

| 属性名 | 类型   | 默认值 | 说明   |
| :----- | :----- | :----- | :----- |
| weekstart | Number | 0 （即周日为第一天）| 以周几为第一天|
| markDays | Array | - | 标记的日期集合 |
| headerBar | Boolean | true | 是否展示月份切换按钮 |
| open | Boolean | true | 是否展开日历 |
| collapsible | Boolean | true | 是否可收缩 |
| disabledAfter | Boolean | false | 未来日期是否不可点击 |

### 事件说明

| 事件名 | 说明   |
| :----- | :----- |
| onDayClick | 点击日期时触发，返回当前选中日期, 如：{date: "2020-07-18", week: "星期六"}  |

### 方法说明

| 方法名 | 说明   |
| :----- | :----- |
| changYearMonth | 改变年月，初始化时需在onReady后通过$refs调用  |
