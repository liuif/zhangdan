<template>
	<view class="content">
		<view class="username">
			<text class="username-text">{{ username }}</text>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
		    return {
		      avatarUrl: '/static/img/touxiang.jpg',
		      totalAsset: "1000",
		      totalDebt: "500",
			  username:uni.getStorageSync('name')
		    }
			console.log(uni.getStorageSync('name'));
		  }

	}
</script>

<style>
	.content {
		text-align: center;
		height: 400upx;
		margin-top: 200upx;
	}
</style>
