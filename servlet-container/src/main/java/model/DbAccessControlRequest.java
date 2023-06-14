package model;

import java.util.Date;

import com.github.pohtml.annotations.Get;

@Get("intranet")
class DbAccessControlRequest {
	Date timestamp = new Date();
	Person[] persons = {
		new Person("Santiago", "Ruiz Hiebra", "Albatros"),
		new Person("Félix", "Casas Abad", "Mr Robot")
	};
}