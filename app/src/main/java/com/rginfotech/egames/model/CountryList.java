package com.rginfotech.egames.model;

public class CountryList {

    private String id;
    private String code;
    private String iso3;
    private String iso_numeric;
    private String fips;
    private String name;
    private String asciiname;
    private String asciiname_ar;
    private String capital;
    private String area;
    private String population;
    private String continent_code;
    private String tld;
    private String currency_code;
    private String phone;
    private String postal_code_format;
    private String postal_code_regex;
    private String languages;
    private String neighbours;
    private String equivalent_fips_code;
    private String flag;
    private String admin_type;
    private String admin_field_active;
    private String active;
    private String delivery_charge;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIso3() {
        return iso3;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    public String getIso_numeric() {
        return iso_numeric;
    }

    public void setIso_numeric(String iso_numeric) {
        this.iso_numeric = iso_numeric;
    }

    public String getFips() {
        return fips;
    }

    public void setFips(String fips) {
        this.fips = fips;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAsciiname() {
        return asciiname;
    }

    public void setAsciiname(String asciiname) {
        this.asciiname = asciiname;
    }

    public String getAsciiname_ar() {
        return asciiname_ar;
    }

    public void setAsciiname_ar(String asciiname_ar) {
        this.asciiname_ar = asciiname_ar;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getContinent_code() {
        return continent_code;
    }

    public void setContinent_code(String continent_code) {
        this.continent_code = continent_code;
    }

    public String getTld() {
        return tld;
    }

    public void setTld(String tld) {
        this.tld = tld;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPostal_code_format() {
        return postal_code_format;
    }

    public void setPostal_code_format(String postal_code_format) {
        this.postal_code_format = postal_code_format;
    }

    public String getPostal_code_regex() {
        return postal_code_regex;
    }

    public void setPostal_code_regex(String postal_code_regex) {
        this.postal_code_regex = postal_code_regex;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(String neighbours) {
        this.neighbours = neighbours;
    }

    public String getEquivalent_fips_code() {
        return equivalent_fips_code;
    }

    public void setEquivalent_fips_code(String equivalent_fips_code) {
        this.equivalent_fips_code = equivalent_fips_code;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getAdmin_type() {
        return admin_type;
    }

    public void setAdmin_type(String admin_type) {
        this.admin_type = admin_type;
    }

    public String getAdmin_field_active() {
        return admin_field_active;
    }

    public void setAdmin_field_active(String admin_field_active) {
        this.admin_field_active = admin_field_active;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getDelivery_charge() {
        return delivery_charge;
    }

    public void setDelivery_charge(String delivery_charge) {
        this.delivery_charge = delivery_charge;
    }


    @Override
    public String toString() {
        return asciiname +" "+code;
    }
}
