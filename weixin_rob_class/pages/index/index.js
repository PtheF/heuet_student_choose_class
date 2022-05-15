Page({

  /**
   * 页面的初始数据
   */
  data: {
    id: "",
    password: ""
  },

  changeId(e){
    // console.log(e);
    this.setData({id: e.detail.value})
  },

  changePw(e){
    // console.log(e);
    this.setData({password: e.detail.value})
  },

  login(){
    console.log(this.data.id);
    console.log(this.data.password);

    let loginRequest = {
      id: this.data.id,
      pw: this.data.password
    }

    wx.request({
      url: "http://localhost:8080/user/login",
      method: "POST",
      data: loginRequest,
      contentType : 'application/json',
      success: (res) => {
        let data = res.data;

        if(data.success){
          
          wx.setStorage({
            key: "user", 
            data: data.data
          });

          wx.setStorage({
            key: "authorization",
            data: data.authorization
          })

          wx.navigateTo({
            url: "../route/route"
          });
        }else{
          wx.showModal({
            title: "异常",
            content: "学号或密码错误",
            cancelColor: 'cancelColor',
          })
        }
      },
      fail: (err) => {
        wx.showModal({
          title: "异常",
          content: "服务异常，请稍等重试",
          cancelColor: 'cancelColor',
        })
      }
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
    
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
    
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
    
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {
    
  }
})