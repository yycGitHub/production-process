/*
 * Copyright (c) 2017-2018 THL A29 Limited, a Tencent company. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tencentcloudapi.vod.v20180717.models;

import com.tencentcloudapi.common.AbstractModel;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;
import java.util.HashMap;

public class MediaProcessTaskAdaptiveDynamicStreamingResult  extends AbstractModel{

    /**
    * 任务状态，有 PROCESSING，SUCCESS 和 FAIL 三种。
    */
    @SerializedName("Status")
    @Expose
    private String Status;

    /**
    * 错误码，0：成功，其他值：失败。
    */
    @SerializedName("ErrCode")
    @Expose
    private Long ErrCode;

    /**
    * 错误信息。
    */
    @SerializedName("Message")
    @Expose
    private String Message;

    /**
    * 对视频转自适应码流任务的输入。
    */
    @SerializedName("Input")
    @Expose
    private AdaptiveDynamicStreamingTaskInput Input;

    /**
    * 对视频转自适应码流任务的输出。
    */
    @SerializedName("Output")
    @Expose
    private AdaptiveDynamicStreamingInfoItem Output;

    /**
     * 获取任务状态，有 PROCESSING，SUCCESS 和 FAIL 三种。
     * @return Status 任务状态，有 PROCESSING，SUCCESS 和 FAIL 三种。
     */
    public String getStatus() {
        return this.Status;
    }

    /**
     * 设置任务状态，有 PROCESSING，SUCCESS 和 FAIL 三种。
     * @param Status 任务状态，有 PROCESSING，SUCCESS 和 FAIL 三种。
     */
    public void setStatus(String Status) {
        this.Status = Status;
    }

    /**
     * 获取错误码，0：成功，其他值：失败。
     * @return ErrCode 错误码，0：成功，其他值：失败。
     */
    public Long getErrCode() {
        return this.ErrCode;
    }

    /**
     * 设置错误码，0：成功，其他值：失败。
     * @param ErrCode 错误码，0：成功，其他值：失败。
     */
    public void setErrCode(Long ErrCode) {
        this.ErrCode = ErrCode;
    }

    /**
     * 获取错误信息。
     * @return Message 错误信息。
     */
    public String getMessage() {
        return this.Message;
    }

    /**
     * 设置错误信息。
     * @param Message 错误信息。
     */
    public void setMessage(String Message) {
        this.Message = Message;
    }

    /**
     * 获取对视频转自适应码流任务的输入。
     * @return Input 对视频转自适应码流任务的输入。
     */
    public AdaptiveDynamicStreamingTaskInput getInput() {
        return this.Input;
    }

    /**
     * 设置对视频转自适应码流任务的输入。
     * @param Input 对视频转自适应码流任务的输入。
     */
    public void setInput(AdaptiveDynamicStreamingTaskInput Input) {
        this.Input = Input;
    }

    /**
     * 获取对视频转自适应码流任务的输出。
     * @return Output 对视频转自适应码流任务的输出。
     */
    public AdaptiveDynamicStreamingInfoItem getOutput() {
        return this.Output;
    }

    /**
     * 设置对视频转自适应码流任务的输出。
     * @param Output 对视频转自适应码流任务的输出。
     */
    public void setOutput(AdaptiveDynamicStreamingInfoItem Output) {
        this.Output = Output;
    }

    /**
     * 内部实现，用户禁止调用
     */
    public void toMap(HashMap<String, String> map, String prefix) {
        this.setParamSimple(map, prefix + "Status", this.Status);
        this.setParamSimple(map, prefix + "ErrCode", this.ErrCode);
        this.setParamSimple(map, prefix + "Message", this.Message);
        this.setParamObj(map, prefix + "Input.", this.Input);
        this.setParamObj(map, prefix + "Output.", this.Output);

    }
}

