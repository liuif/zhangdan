<template>
	<view class="datatime">
		<picker mode="multiSelector" :range="range" range-key="text" @change="change" @columnchange="columnchange"
			:value="value" class="item-bootom1">
			<view class="uni-input1">
				<view class="pickerText1" :class="{selectTrue1:!dateStr}">
					{{dateStr?dateStr:placeholder}}
				</view>
				<view class="pickerSelect1">选择</view>
			</view>
		</picker>

	</view>
</template>

<script>
	export default {
		props: {
			// 占位符
			placeholder: {
				type: String,
				default: '请选择'
			},
			// 表示有效日期时间范围的开始，
			// 字符串格式为 "YYYY-MM"
			start: {
				type: String,
				default: '1950-01'
			},
			// 表示有效日期时间范围的结束
			// 字符串格式为 "YYYY-MM"
			end: {
				type: String,
				default: '2300-12'
			},
			// 字符串格式为 "YYYY-MM"
			defaultValue: {
				type: String,
				default: ''
			}
		},
		data() {
			return {
				range: [
					[],
					[]
				],
				value: [],
				dateStr: '', // 最终显示的字符串
				dtStart: null, // 有效范围开始
				dtEnd: null, // 有效范围结束
				range_m: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"]
			};
		},
		watch: {
			// 默认值
			defaultValue() {
				// 设置默认值
				this.setDefaultValue();
			}
		},
		created() {
			// 有效日期开始和结束
			this.dtStart = this.start;
			this.dtEnd = this.end;
			this.getlist()
		},
		methods: {
			getlist: function() {
				let start_y = this.dtStart.slice(0, 4)
				let end_y = +this.dtEnd.slice(0, 4)
				for (let i = start_y; i < end_y + 1; i++) {
					this.range[0].push(i + "年")
				}
				for (let j = 1; j < 13; j++) {
					this.range[1].push(j + "月")
				}
				this.setDefaultValue()
			},
			columnchange: function(e) {
				if (e.detail.column == 0) {
					this.value[0] = e.detail.value
					this.value[1] = 0
					this.$set(this.value, 1, 0)
				} else {
					this.value[1] = e.detail.value
				}
				this.getmoth()
			},
			change: function(e) {
				let aa = e.detail.value
				let _new_y = this.range[0][aa[0]].slice(0, 4) + "-"
				let _new_m = this.range[1][aa[1]].slice(0, 2)
				this.$emit("change", _new_y + _new_m)
			},
			setDefaultValue: function() {
				let _this = this
				let new_y = ""
				let new_m = ""
				if (this.defaultValue) {
					new_y = this.defaultValue.slice(0, 4)
					new_m = this.defaultValue.slice(5, 7)
					this.dateStr = new_y + "-" + new_m.padStart(2, "0")
				} else {
					let d = new Date()
					new_y = this.dtStart.slice(0, 4)
					new_m = this.dtStart.slice(5, 7)
				}
				this.range.forEach(arritem => {
					arritem.forEach((item, index) => {
						if (item == new_y + "年") {
							_this.value[0] = index
						}
						if (item == new_m + "月") {
							_this.value[1] = index
						}
					})
				})

				this.getmoth()
			},
			getmoth: function() {
				if (this.range[0][this.value[0]] == this.dtStart.slice(0, 4) + "年") {
					let start_m = +this.dtStart.slice(5, 7)
					this.range[1] = this.range[1].slice(start_m - 1, 12)
				} else if (this.range[0][this.value[0]] == this.dtEnd.slice(0, 4) + "年") {
					let end_m = +this.dtEnd.slice(5, 7)
					this.range[1] = this.range[1].slice(0, end_m)
				} else {
					this.range[1] = []
					this.range_m.forEach(item => {
						this.range[1].push(item)
					})

				}
			}

		}
	};
</script>

<style lang="scss" scoped>
	.item-bootom1 {
		height: 80rpx;
		padding: 0px 8rpx;

		font-size: 32rpx;

		&.input {
			color: #007AFF;
		}
	}

	.uni-input1 {
		height: 80rpx;
		display: flex;
		align-items: center;
		border-bottom: 1rpx solid #d9d9d9;
	}

	.pickerText1 {
		flex: 1;
		height: 80rpx;
		display: flex;
		align-items: center;
		color: #4399FC;
		font-size: 32rpx;
	}

	.pickerSelect1 {
		color: #007AFF;
	}

	.itemText1 {
		// width: 160rpx;
		height: 60rpx;
		padding: 0px 16rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		background-color: #d2d2d2;
		color: #007AFF;
	}

	.selectTrue1 {
		color: #b1b1b1 !important;
	}
</style>
