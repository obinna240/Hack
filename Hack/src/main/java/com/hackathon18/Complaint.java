package com.hackathon18;

import java.util.Optional;

public class Complaint {
	private String uuid;
	private Optional<String> product;
	private Optional<String> sub_product;
	private Optional<String> sub_issue;
	private Optional<String> complaint;
	private Optional<String> issue;
	
	public Optional<String> getProduct() {
		return product;
	}
	public void setProduct(Optional<String> product) {
		this.product = product;
	}
	public Optional<String> getSub_product() {
		return sub_product;
	}
	public void setSub_product(Optional<String> sub_product) {
		this.sub_product = sub_product;
	}
	public Optional<String> getSub_issue() {
		return sub_issue;
	}
	public void setSub_issue(Optional<String> sub_issue) {
		this.sub_issue = sub_issue;
	}
	public Optional<String> getComplaint() {
		return complaint;
	}
	public void setComplaint(Optional<String> complaint) {
		this.complaint = complaint;
	}
	public Optional<String> getIssue() {
		return issue;
	}
	public void setIssue(Optional<String> issue) {
		this.issue = issue;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
