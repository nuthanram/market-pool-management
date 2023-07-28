package com.techno.mpm.pojo.candidate;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(value = Include.NON_DEFAULT)
public class ResumePojo implements Serializable {

	
	private byte[] resumeDownload;
	private String fileName;
	private String fileExtension;
	private String filePath;
}
