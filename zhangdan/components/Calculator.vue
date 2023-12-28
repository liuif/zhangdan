<template>
	<view class="digit-keyboard">
		<view class="digit-keyboard_bg" @tap="hide"></view>
		<view class="digit-keyboard_area">
			<!-- 金额 -->
			<view class="money-box">
				<text style="font-size: 30rpx;margin-right: 20rpx;">{{title}}</text>
				<text>{{str}}</text>
			</view>
			<u-line color="info" length='0' />
			<view class="input-area">
				<view class="item">
					<u-icon name="order" size="36"></u-icon>
				</view>
				<view class="item" style="margin-left: 30rpx;">
					<u-input height='55' placeholder="点击填写备注..." v-model="remark" :border='false' />
				</view>
			</view>
			<view class="number-area">
				<view class="item" v-if='showToday' style="color: #4d7df9;" @click='clickPick'>
					今天
				</view>
				<view class="item item2" v-else @click='clickPick'>
					{{dayVal}}
				</view>
				<view class="item" @click="modifyNum('clear')">C</view>
				<!-- cut -->
				<view class="item" @click="modifyNum('cut')">
					<u-icon name="backspace" size="36"></u-icon>
				</view>
				<view class="item" @click='cancel'>
					取消
				</view>
				<!-- <view class="item" @tap="modifyNum('operator','/')">/</view> -->
				<view class="item" @tap="modifyNum('number','7')">7</view>
				<view class="item" @tap="modifyNum('number','8')">8</view>
				<view class="item" @tap="modifyNum('number','9')">9</view>
				<!-- 加 -->
				<view class="item" @tap="modifyNum('operator','+')">+</view>
				<view class="item" @tap="modifyNum('number','4')">4</view>
				<view class="item" @tap="modifyNum('number','5')">5</view>
				<view class="item" @tap="modifyNum('number','6')">6</view>
				<!-- 减 -->
				<view class="item" @tap="modifyNum('operator','-')">-</view>
				<view class="item" @tap="modifyNum('number','1')">1</view>
				<view class="item" @tap="modifyNum('number','2')">2</view>
				<view class="item" @tap="modifyNum('number','3')">3</view>
				<view class="item" @tap="modifyNum('equal')">=</view>
				<!-- <view class="item" @tap="modifyNum('operator','%')">%</view> -->
				<view class="item" @tap="modifyNum('string','.')">.</view>
				<view class="item" @tap="modifyNum('number','0')">0</view>
				<!-- 乘 -->
				<view class="item" @tap="modifyNum('operator','*')">*</view>
				<view class="item" @tap="finish" style="background-color: #4d7df9;color: white;">完成</view>
			</view>
		</view>
		<u-picker mode="time" v-model="showPicker" :params="timeParams" @confirm='pickerConfirm'></u-picker>
	</view>
</template>

<script>
	export default {
		props: {
			title: {
				type: String,
				default: ''
			}
		},
		data() {
			return {
				remark: '', //备注
				str: '', //金额部分
				showPicker: false, //显示时间选择器
				timeParams: {
					year: true,
					month: true,
					day: true,
					hour: false,
					minute: false,
					second: false
				},
				dayVal: '', //今天位置
				timeObj: null,
				showToday: true,

				/*
				*currentNumber: 用于跟踪当前输入的数字或操作数。当用户按下数字按钮时，这个变量将被更新以包含新的数字字符。
				* 例如，当用户按下数字键 "1"、"2"、"3" 时，currentNumber 将依次变为 "1"、"12"、"123"。
				
				currentOperator: 用于跟踪当前选定的操作符，例如加法 "+"、减法 "-"、乘法 "*"、除法 "/"。
				* 如果用户按下运算符按钮，这个变量将被设置为相应的运算符。
				
				previousValue: 用于存储上一个操作的结果，以便进行下一个操作时使用。
				* 例如，如果用户按下 "1 + 2"，previousValue 将存储 "1"，然后在按下 "=" 后，将 "previousValue + currentNumber" 计算为最终结果。
				
				isDecimal: 用于跟踪当前数字是否包含小数点。当用户按下小数点按钮时，这个标志将设置为 true，以确保小数点只能出现一次。
				* */
				state: {
					currentNumber: '',
					currentOperator: '',
					previousValue: null,
					isDecimal: false,
				}
			};
		},
		created() {
			let date = new Date()
			let year = date.getFullYear()
			let month = date.getMonth() < 10 ? '0' + date.getMonth() : date.getMonth()
			let day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate()
			this.dayVal = year + '/' + month + '/' + day
			this.timeObj = {
				year: year.toString(),
				month: month.toString(),
				day
			}
		},
		methods: {
			//点击空白处隐藏
			hide() {
				this.$emit('cancel');
			},

			//取消
			cancel() {
				this.$emit('cancel');
			},

			//出现时间选择器
			clickPick() {
				this.showPicker = true
			},

			//选择日期
			pickerConfirm(obj) {
				this.showToday = false
				this.dayVal = obj.year + '/' + obj.month + '/' + obj.day
				this.timeObj = obj
			},

			modifyNum(type, value) {
				if (type === 'number') {
					if (this.state.isDecimal) {
						const decimalCount = this.state.currentNumber.split('.')[1];
						if (decimalCount && decimalCount.length >= 2) {
							return;
						}
						this.state.currentNumber += value;
					} else {
						if (this.state.currentNumber === '0') {
							this.state.currentNumber = value;
						} else {
							this.state.currentNumber += value;
						}
					}
					this.str += value; // 更新表达式
				} else if (type === 'operator') {
					if (this.state.currentNumber !== '') {
						if (this.state.currentOperator !== '') {
							// 如果之前已经有运算符，先计算上一步的结果
							this.calculate();
						}
						this.state.previousValue = parseFloat(this.state.currentNumber);
						this.state.currentNumber = '';
						this.state.isDecimal = false;
						this.str += `${value}`; // 添加运算符到表达式
					}
					this.state.currentOperator = value;
				} else if (type === 'equal') {
					if (this.state.currentOperator && this.state.currentNumber !== '') {
						this.str += `${this.state.currentNumber}`; // 显示完整表达式
						this.calculate(); // 计算结果
						this.state.currentOperator = '';
					}
				} else if (type === 'string') {
					if (this.state.currentNumber === '') {
						this.state.currentNumber = '0';
						this.str = '0'; // 更新表达式
					}

					if (value === '.' && this.state.isDecimal) {
						return; // 如果已经输入小数点，不再输入
					}
					this.state.isDecimal = value === '.';
					this.state.currentNumber += value;
					this.str += value; // 更新表达式
				} else if (type === 'clear') {
					this.clear();
				} else if (type === 'cut') {
					this.cut();
				}
			},
			calculate() {
				const num1 = this.state.previousValue;
				const num2 = parseFloat(this.state.currentNumber);
				let result = 0;

				switch (this.state.currentOperator) {
					case '+':
						result = num1 + num2;
						break;
					case '-':
						result = num1 - num2;
						break;
					case '*':
						result = num1 * num2;
						break;
					case '/':
						result = num1 / num2;
						break;
				}

				this.state.currentNumber = result.toString();
				this.state.isDecimal = this.state.currentNumber.includes('.');
				this.str = parseFloat(this.state.currentNumber);
			},

			clear() {
				this.state.currentNumber = '';
				this.state.currentOperator = '';
				this.state.previousValue = null;
				this.state.isDecimal = false;
				this.str = this.state.currentNumber;
			},

			cut() {
				if (this.state.currentNumber !== '' || this.state.currentOperator !== '') {
					// 删除最后一个字符，无论是数字还是运算符
					const lastChar = this.str.toString().slice(-1);
					this.str = this.str.toString().slice(0, -1);

					//解决输入一个运算符后，删除之后，不能再输入运算符
					if (lastChar === '+' || lastChar === '-' || lastChar === '*') {
						this.state.currentOperator = '';
						this.state.currentNumber = this.str
					} else if (this.state.currentNumber !== '') {
						// 如果最后一个字符是数字，则更新当前数字
						this.state.currentNumber = this.state.currentNumber.slice(0, -1);
					}
				} else if (this.state.previousValue !== null) {
					// 如果以上条件都不满足，则删除前一个值的最后一个字符
					const previousValueString = this.state.previousValue.toString();
					if (previousValueString.length > 1) {
						this.state.previousValue = parseFloat(previousValueString.slice(0, -1));
					} else {
						this.state.previousValue = null;
					}
				}

				//解决输入小数点之后，删了小数点，之后不能再输入小数点问题
				this.state.isDecimal = this.state.currentNumber.includes('.')
				//避免当输入的算式清空后，还在以上个运算结果为初始值继续运算
				if (this.str === '') {
					this.state.currentNumber = '';
					this.state.currentOperator = '';
					this.state.previousValue = null;
				}
			},

			//完成
			finish() {
				if (this.state.currentOperator && this.state.currentNumber !== '') {
					this.calculate();
					this.state.currentOperator = '';
				}
				let obj = {
					money: Number(this.str),
					time: this.timeObj,
					remark: this.remark
				}
				this.$emit('finish', obj);
			},
		}
	}
</script>


<style lang="scss" scoped>
	.digit-keyboard {
		position: fixed;
		left: 0;
		top: 0;
		width: 100%;
		height: 100%;
		z-index: 999;
	}

	.digit-keyboard_bg {
		width: 100%;
		height: 100%;
		background: rgba($color: #000000, $alpha: 0);
	}

	.digit-keyboard_area {
		position: absolute;
		bottom: 0;
		left: 0;
		width: 100%;
		background: #efefef;
	}

	/* 类名名称和金额 */
	.money-box {
		height: 100rpx;
		font-size: 50rpx;
		line-height: 100rpx;
		background-color: white;
		display: flex;
		justify-content: space-between;
		padding: 0 30rpx;
		box-sizing: border-box;
		overflow-x: auto;
	}

	.input-area {
		display: flex;
		align-items: center;
		padding: 18upx;
		background: #FFFFFF;

		.item {
			font-size: 28upx;

			&:nth-of-type(2) {
				flex: 1 0 auto;
				padding-right: 10upx;
			}

			&:nth-of-type(3) {
				font-size: 0;
			}
		}

		.input {
			background: #eeeeee;
			text-indent: 10upx;
			font-size: 28upx;
			height: 60upx;
		}
	}

	.number-area {
		display: flex;
		justify-content: space-around;
		flex-wrap: wrap;
		text-align: center;
		background-color: #ffffff;
		padding-bottom: 20rpx;

		.item {
			margin-top: 20upx;
			flex: 0 0 22%;
			background: #FFFFFF;

			font-size: 30upx;
			font-weight: bold;

			width: 80rpx;
			height: 80rpx;
			line-height: 80rpx;
			border-radius: 50rpx;
			box-shadow: 0 4rpx 24rpx 0 rgba(0, 0, 0, 0.1);
		}

		.item2 {
			font-size: 24upx;
			color: #5A9BF8;
		}
	}
</style>