Function Ph_DoCollision(t#, maxabs#, k#, recnr=0)
	Local obj.Ph_Object
	Local obj2.Ph_Object
	Local virtual.Ph_Object
	Local Temp#[1]
	Local Temp1#[1]
	Local Temp2#[1]
	Local tBank
	obj = First Ph_Object
	If obj=Null Then Return
	If obj\CollisionBox=Null Then Return
	Local flag=False
	Repeat
		obj2 = After obj
		If obj2=Null Then Return
		If obj2\CollisionBox=Null Then Return
		Repeat
			If Ph_CollideObjectAfterTime(obj, obj2, t) Then
				Local i=1
				Local t2#=0.5
				Local lt#=1
				Repeat
					i=i+1
					If Ph_CollideObjectAfterTime(obj, obj2, t*t2) Then
						lt=t2
						t2=t2-(1/(2^i))
					Else
						t2=t2-(1/(2^i))
					EndIf
				Until (t2*t)*VectorLenght(obj\Vel)<maxabs And (t2*t)*VectorLenght(obj2\Vel)<maxabs
				
				If Ph_CollideObjectAfterTime(obj, obj2, t*t2) Then
					lt=t2
					t2=t2-(1/(2^i))
				EndIf
				tBank = Ph_CollideObjectAfterTime(obj, obj2, t*lt)
				Temp[0] = PeekFloat(tBank,0)
				Temp[1] = PeekFloat(tBank,4)
				
				Ph_DoTick(obj, t*lt)
				Ph_DoTick(obj2, t*lt)
				
				Ph_RelativatePosition(obj,Temp,Temp1)
				Ph_RelativatePosition(obj2,Temp,Temp2)
				
				Ph_ApplyCollision(obj,obj2, Temp1, Temp2, PeekFloat(tBank,8), k, t)
				flag=True
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
;	MainPhysicRender()
;	Flip
;	WaitKey()
	If flag And recnr<100 Then Ph_DoCollision(t, maxabs, k, recnr+1)
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

Function Ph_ApplyCollision(obj1.Ph_Object,obj2.Ph_Object, pos_obj1#[1], pos_obj2#[1], angle#, k#, t#)
	; TODO Apply the Collision Forces / Stoesse
	
	Local PVel1#[1], PVel2#[1]
	
	RotateVector(pos_obj1,obj1\RotVel,PVel1)
	SubtractVector(pos_obj1,PVel1, PVel1)
	AddVector(PVel1, obj1\Vel, PVel1)
	
	RotateVector(pos_obj2,obj2\RotVel,PVel2)
	SubtractVector(pos_obj2, PVel2, PVel2)
	AddVector(PVel2, obj2\Vel, PVel2)
	
	If obj1\Fixed And obj2\Fixed Then Return
	
	Local obj1velB#[1]
	Local obj2velB#[1]
	Local obj1velUB#[1]
	Local obj2velUB#[1]
	Local Temp1#, Temp2#
	
	; Berechnug der unbeeinflussten Teile (vel1)
	
	obj1velUB[0]=1
	obj1velUB[1]=0	
	RotateVector(obj1velUB,angle + Pi,obj1velUB)
	
	obj2velUB[0]=1
	obj2velUB[1]=0	
	RotateVector(obj2velUB,angle,obj2velUB)
	
	Local temp#[1]
	temp[0]=1
	temp[1]=0	
	
	If VectorLenght(PVel1) = 0 Then Temp1 = 0 Else Temp1=Sin(90-RadToDeg(VectorAngle(PVel1,temp)-angle))*VectorLenght(PVel1)
	If VectorLenght(PVel2) = 0 Then Temp2 = 0 Else Temp2=Sin(90-RadToDeg(VectorAngle(PVel2,temp)-angle))*VectorLenght(PVel2)
	
	MultiplyVector(obj1velUB, Temp1, obj1velUB)
	MultiplyVector(obj2velUB, Temp2, obj2velUB)
	
	; Berechung der beeinflussten Teile der Geschwindigkeit (vel2)
	
	obj1velB[0]=1
	obj1velB[1]=0	
	RotateVector(obj1velB,angle-(Pi/2),obj1velB)
	
	obj2velB[0]=1
	obj2velB[1]=0	
	RotateVector(obj1velB,angle+(Pi/2),obj1velB)
	
	
	If VectorLenght(PVel1) = 0 Then Temp1 = 0 Else Temp1=Sin(RadToDeg(VectorAngle(PVel1,temp)-angle))*VectorLenght(PVel1)
	If VectorLenght(PVel2) = 0 Then Temp2 = 0 Else Temp2=Sin(RadToDeg(VectorAngle(PVel2,temp)-angle))*VectorLenght(PVel2)
	
	
	MultiplyVector(obj1velB, Temp1, obj1velB)
	MultiplyVector(obj2velB, Temp2, obj2velB)
	
	; Bewegung in Collisionsrichtung ausbremsen
	
	;- obj1
	
	If VectorLenght(obj1velUB)<>0 Then
		angle = VectorAngle(pos_obj1, obj1velUB)
		obj1\Vel[0]=1
		obj1\Vel[1]=0
		RotateVector(obj1\Vel, (Pi/2) - angle, obj1\Vel)
		MultiplyVector(obj1\Vel, Sin((90-RadToDeg(angle))*VectorLenght(obj1velUB)), obj1\Vel)
		obj1\RotVel=obj1\RotVel+((Sin(RadToDeg(angle))*VectorLenght(obj1velUB))/VectorLenght(pos_obj1))
	EndIf
	
	; - obj2
	
	If VectorLenght(obj2velUB)<>0 Then
		angle = VectorAngle( obj2velUB, pos_obj2 )
		obj2\Vel[0]=1
		obj2\Vel[1]=0
		RotateVector(obj2\Vel, (Pi/2) - angle, obj2\Vel)
		MultiplyVector(obj2\Vel, Sin((90-RadToDeg(angle))*VectorLenght(obj2velUB)), obj2\Vel)
		obj2\RotVel=obj2\RotVel+((Sin(RadToDeg(angle))*VectorLenght(obj2velUB))/VectorLenght(pos_obj2))
	EndIf
		
	; berechnung der Stosses
	
	MultiplyVector(obj1velB, obj1\Mass / t, temp)
	Ph_ApplyForce(obj2, temp, pos_obj2)
	
	MultiplyVector(obj2velB, obj2\Mass / t, temp)
	Ph_ApplyForce(obj1, temp, pos_obj1)
	
	; Reibung berechnen
	
	MultiplyVector(obj1velUB, obj1\Mass / t, temp)
	MultiplyVector(temp, (obj1\friction_velue+obj2\friction_velue)/2, temp)
	MultiplyVector(temp, -1, temp)
	Ph_ApplyForce(obj2, temp, pos_obj2)
	
	MultiplyVector(obj2velB, obj2\Mass / t, temp)
	MultiplyVector(temp, (obj1\friction_velue+obj2\friction_velue)/2, temp)
	MultiplyVector(temp, -1, temp)
	Ph_ApplyForce(obj1, temp, pos_obj1)
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D