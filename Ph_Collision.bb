Function Ph_DoCollision(t#, maxabs#)
	Local obj.Ph_Object
	Local obj2.Ph_Object
	Local virtual.Ph_Object
	Local Temp#[1]
	Local Temp1#[1]
	Local Temp2#[1]
	Local tBank
	obj = First Ph_Object
	Repeat
		obj2 = After obj
		Repeat
			If Ph_CollideObjectAfterTime(obj, obj2, t) Then
				Local i=1
				Local t2#=0.5
				Repeat
					i=i+1
					If Ph_CollideObjectAfterTime(obj, obj2, t*t2) Then
						t2=t2-(1/(2^i))
					Else
						t2=t2-(1/(2^i))
					EndIf
				Until (t2*t)*VectorLenght(obj\Vel)<maxabs And (t2*t)*VectorLenght(obj2\Vel)<maxabs
				
				If Ph_CollideObjectAfterTime(obj, obj2, t*t2) Then
					t2=t2-(1/(2^i))
				EndIf
				tBank = Ph_CollideObjectAfterTime(obj, obj2, t*(t2+(1/(2^i))))
				Temp[0] = PeekFloat(tBank,0)
				Temp[1] = PeekFloat(tBank,4)
				
				Ph_RelativatePosition(obj,Temp,Temp1)
				Ph_RelativatePosition(obj2,Temp,Temp2)
				
				Ph_ApplyCollision(obj,obj2, Temp1, Temp2, PeekFloat(tBank,8), 1)
				
			EndIf
			If obj2 = Last Ph_Object Then
				Exit
			Else
				obj2=After obj2
			EndIf
		Forever
		obj = After obj
		If obj = Last Ph_Object Then Exit
	Forever
	
End Function

Function Ph_CollideObjectAfterTime(obj1.Ph_Object, obj2.Ph_Object, t#)
	Local virtual1.Ph_Object = Ph_GetVirtualCopyAfterTime(obj1,t)
	Local virtual2.Ph_Object = Ph_GetVirtualCopyAfterTime(obj2,t)
	
	Local virtualShape1.Shape = Ph_GetAbsolutCollisionBox(virtual1)
	Local virtualShape2.Shape = Ph_GetAbsolutCollisionBox(virtual2)
	
	Local rBank = Ph_CollisionBoxColliding(virtualShape1,virtualShape2)
	
	Delete virtual1
	Delete virtual2
	Delete virtualShape1
	Delete virtualShape2
	
	Return rBank
End Function

Function Ph_ApplyCollision(obj1.Ph_Object,obj2.Ph_Object, pos_obj1#[1], pos_obj2#[1], angle#, k#)
	; TODO Apply the Collision Forces / Stoesse
	
	
	Local obj1vel1norm#[1]
	Local obj2vel1norm#[1]
	Local obj1vel1Lenght#
	Local obj2vel1Lenght#
	Local obj1vel2norm#[1]
	Local obj2vel2norm#[1]
	Local obj1vel2Lenght#
	Local obj2vel2Lenght#
	Local obj1vel2LenghtC#
	Local obj2vel2LenghtC#
	
	; Berechnug der unbeeinflussten Teile (vel1)
	
	obj1vel1norm[0]=1
	obj1vel1norm[1]=0	
	RotateVector(obj1vel1norm,angle,obj1vel1norm)
	
	obj2vel1norm[0]=1
	obj2vel1norm[1]=0	
	RotateVector(obj2vel1norm,angle,obj2vel1norm)
	
	Local temp#[1]
	temp[0]=1
	temp[1]=0	
	
	obj1vel1Lenght=Sin(90-RadToDeg(VectorAngle(obj1\Vel,temp)-angle))*VectorLenght(obj1\Vel) ; why Illegal Type-conversion???
	obj2vel1Lenght=Sin(90-RadToDeg(VectorAngle(obj2\Vel,temp)-angle))*VectorLenght(obj2\Vel)
	
	; Berechung der beeinflussten Teile der Geschwindigkeit (vel2)
	
	obj1vel2norm[0]=1
	obj1vel2norm[1]=0	
	RotateVector(obj1vel2norm,angle-(Pi/2),obj1vel2norm)
	
	obj2vel2norm[0]=1
	obj2vel2norm[1]=0	
	RotateVector(obj2vel2norm,angle-(Pi/2),obj2vel2norm)
	
	obj1vel2Lenght=Sin(RadToDeg(VectorAngle(obj1\Vel,temp)-angle))*VectorLenght(obj1\Vel)
	obj2vel2Lenght=Sin(RadToDeg(VectorAngle(obj2\Vel,temp)-angle))*VectorLenght(obj2\Vel)
	
	; berechnung der Stosses
	
	obj1vel2LenghtC = (obj1\Mass*obj1vel2Lenght + obj2\Mass*obj2vel2Lenght - obj2\Mass*(obj1vel2Lenght-obj2vel2Lenght)*k)/(obj1\Mass + obj2\Mass)
	obj1vel2LenghtC = (obj1\Mass*obj1vel2Lenght + obj2\Mass*obj2vel2Lenght - obj1\Mass*(obj1vel2Lenght-obj1vel2Lenght)*k)/(obj1\Mass + obj2\Mass)
	
	; berechnug der Endgeschwindigkeit (Vectoraddition)
	
	MultiplyVector(obj1vel1norm,obj1vel1Lenght,obj1vel1norm)
	MultiplyVector(obj1vel2norm,obj1vel2Lenght,obj1vel2norm)
	MultiplyVector(obj2vel1norm,obj2vel1Lenght,obj2vel1norm)
	MultiplyVector(obj2vel2norm,obj2vel2Lenght,obj2vel2norm)
	
	AddVector(obj1vel1norm, obj1vel2norm, obj1\Vel)
	AddVector(obj2vel1norm, obj2vel2norm, obj2\Vel)
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D