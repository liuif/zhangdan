<template>
  <view class="content">
	  <view class="top">
		  <view class="iconarea">
			  <view class="iconfont icon-jiantou-zuo" style="font-size: 16px;"></view>
		  </view>
		  <button class="InandOut" style="margin-left: 100px;" @click="selectType('expense')" :class="{ selected: selectedType === 'expense' }">支出</button>
		  <button class="InandOut" style="margin-right: 140px;" @click="selectType('income')" :class="{ selected: selectedType === 'income' }">收入</button>
	  </view>
	  <view class="medium">
		  <view
		          v-for="(item, index) in icons"
		          :key="index"
		          class="iconfont"
		          :class="{ selected: selectedItem === item }"
		          @tap="selectItem(item)"
		        >
		          {{ item }}
		        </view>
		  <!-- <view class="iconfont icon-icon_food" @tap=""></view>
		   <view class="iconfont icon-icon_shopping"></view>
		   <view class="iconfont icon-icon_traffic"></view>
		   <view class="iconfont icon-icon_houserent"></view>
		  <view class="iconfont icon-icon_daily"></view> -->
	  </view>
	  <view class="bottom">
		  <Calculator v-if="showCalculator" @cancel='cancelBtn' @finish='finishBtn' >
		  
		  cancelBtn() {
		      this.showCalculator = false
		  },
		  finishBtn(data) {
		      console.log('返回的数据',data)
		      this.showCalculator = false
		  }
		  </Calculator>
	  </view>
  </view>
</template>

<script>
	import Calculator from '@/components/Calculator.vue'
	export default {
		components: {
		    Calculator
		},
		data() {
		    return {
		        showCalculator: true,              
		    }
		},
		data() {
		    return {
		      icons: ['icon-icon_food', 'icon-icon_shopping', 'icon-icon_traffic', 'icon-icon_houserent', 'icon-icon_daily'],
		      selectedType: '', // 'expense' or 'income'
		      selectedItem: null,
		      showCalculator: true,
		    };
		  },
		  methods: {
		      selectType(type) {
		        this.selectedType = type;
		      },
		      selectItem(item) {
		        this.selectedItem = item;
		      },
		      cancelBtn() {
		        this.showCalculator = false;
		      },
		      finishBtn(data) {
		        console.log('选择的类型:', this.selectedType);
		        console.log('选择的图标:', this.selectedItem);
		        console.log('输入的数据:', data);
		        // 将数据发送给后端保存的逻辑可以在这里添加
		        this.showCalculator = false;
		      },
		    },
	};
</script>

<style>
	.top{
		width: 100%;
		height: 50px;
		display: flex;
		align-items: center;
		background-color: #007aff;
	}
	.iconarea{
		height: 100%;
		width: 40px;
	}
	.icon-jiantou-zuo{
		padding-left: 10px;
		padding-top: 16.25px;
		font-size: 16px;
	}
	.InandOut{
		width: 40px;
		height: 20px;
		padding: 0;
		/* background-color: black;
		color: azure; */
		
		font-size: 10px;
		line-height: 2;
		border-radius: 15px;
	}
	
	.medium{
		width: 100%;
		height: 337px;
		padding: 20px;
		display: flex; 
		align-items: flex-start;
		/* white-space: nowrap; */
		display: inline-block;
		/* background-color: wheat; */
	}
	.iconfont{
		/* padding-left: 10px;
		padding-top: 16.25px; */
		font-size: 32px;
		width: 32px;
	}
	.u-demo-block__content {
		@include flex;
		align-items: center;
	}
	
	.u-avatar-item {
		margin-right: 30px;
	}
	/* input {
	  width: 80%;
	  height: 50upx;
	  border: 1upx solid #ccc;
	  border-radius: 5upx;
	  padding: 10upx;
	} */

	/* button {
	  width: 80%;
	  height: 50upx;
	  background-color: #007aff;
	  color: #fff;
	  border: none;
	  border-radius: 5upx;
	  font-size: 16upx;
	} */
</style>