package com.ngo.alahmaar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FormListResult {

@SerializedName("personal_informarion")
@Expose
public PersonalInformarion personalInformarion;
@SerializedName("education_information")
@Expose
public EducationInformation educationInformation;
@SerializedName("employment_information")
@Expose
public EmploymentInformation employmentInformation;
@SerializedName("special_needs_information")
@Expose
public SpecialNeedsInformation specialNeedsInformation;
@SerializedName("volunteer_information_link")
@Expose
public VolunteerInformationLink volunteerInformationLink;
@SerializedName("member_declaration")
@Expose
public MemberDeclaration memberDeclaration;
@SerializedName("membership_determination")
@Expose
public MembershipDetermination membershipDetermination;
@SerializedName("red_army_service")
@Expose
public RedArmyService redArmyService;
@SerializedName("armed_forces_service")
@Expose
public ArmedForcesService armedForcesService;
@SerializedName("commendation_information")
@Expose
public CommendationInformation commendationInformation;
@SerializedName("martyrs_reord")
@Expose
public MartyrsReord martyrsReord;

    public PersonalInformarion getPersonalInformarion() {
        return personalInformarion;
    }

    public void setPersonalInformarion(PersonalInformarion personalInformarion) {
        this.personalInformarion = personalInformarion;
    }

    public EducationInformation getEducationInformation() {
        return educationInformation;
    }

    public void setEducationInformation(EducationInformation educationInformation) {
        this.educationInformation = educationInformation;
    }

    public EmploymentInformation getEmploymentInformation() {
        return employmentInformation;
    }

    public void setEmploymentInformation(EmploymentInformation employmentInformation) {
        this.employmentInformation = employmentInformation;
    }

    public SpecialNeedsInformation getSpecialNeedsInformation() {
        return specialNeedsInformation;
    }

    public void setSpecialNeedsInformation(SpecialNeedsInformation specialNeedsInformation) {
        this.specialNeedsInformation = specialNeedsInformation;
    }

    public VolunteerInformationLink getVolunteerInformationLink() {
        return volunteerInformationLink;
    }

    public void setVolunteerInformationLink(VolunteerInformationLink volunteerInformationLink) {
        this.volunteerInformationLink = volunteerInformationLink;
    }

    public MemberDeclaration getMemberDeclaration() {
        return memberDeclaration;
    }

    public void setMemberDeclaration(MemberDeclaration memberDeclaration) {
        this.memberDeclaration = memberDeclaration;
    }

    public MembershipDetermination getMembershipDetermination() {
        return membershipDetermination;
    }

    public void setMembershipDetermination(MembershipDetermination membershipDetermination) {
        this.membershipDetermination = membershipDetermination;
    }

    public RedArmyService getRedArmyService() {
        return redArmyService;
    }

    public void setRedArmyService(RedArmyService redArmyService) {
        this.redArmyService = redArmyService;
    }

    public ArmedForcesService getArmedForcesService() {
        return armedForcesService;
    }

    public void setArmedForcesService(ArmedForcesService armedForcesService) {
        this.armedForcesService = armedForcesService;
    }

    public CommendationInformation getCommendationInformation() {
        return commendationInformation;
    }

    public void setCommendationInformation(CommendationInformation commendationInformation) {
        this.commendationInformation = commendationInformation;
    }

    public MartyrsReord getMartyrsReord() {
        return martyrsReord;
    }

    public void setMartyrsReord(MartyrsReord martyrsReord) {
        this.martyrsReord = martyrsReord;
    }
}