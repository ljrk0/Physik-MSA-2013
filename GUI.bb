Function GUI()
	Local mx,my
	Local Testobject1.Ph_Object = New Ph_Object
	Local Choice$
	Local Form$
	Local XSize%
	Local YSize%
	Local negXSize$
	Local negYSize$
	Local Timer = CreateTimer(30)
	Local LastTime# = MilliSecs()
	Local TimerError = 0
	Repeat
		If WaitTimer(Timer)>1 Then
			TimerError=TimerError+1
			If TimerError = 5 Then RuntimeError "System to slow to keep 30 fp/s - Stopping Simulation causing probapaly Inconsitents"
		EndIf
		
		mx=MouseX()
		my=MouseY()
		Cls
		If MouseHit(1) Then
			Cls
			Flip
			Print"waehle die Form des Objekts!"
			Print"0 fuer Exit, 1 fuer Rechteck, 2 fuer Kreis"
			Choice$=Input("Deine Wahl:")
			Select Choice$
				Case "0" 
					Return
				Case "1"
					Testobject1.Ph_Object = New Ph_Object
					Testobject1\Pos[0] = mx
					Testobject1\Pos[1] = my
					Print"waehle die waagerechte Geschwindigkeit!"
					Choice$=Input("Deine Wahl:")
					Testobject1\Vel[0] = Choice$
					Print"waehle die senkrechte Geschwindigkeit!"
					Choice$=Input("Deine Wahl:")
					Testobject1\Vel[1] = Choice$
					Print"waehle die Masse des Objekts"
					Choice$=Input("Deine Wahl:")
					Testobject1\Mass = Choice$
					Print"waehle die Ausrichtung!"
					Choice$=Input("Deine Wahl:")
					Testobject1\Rot = Choice$
					Print"waehle die x-Abmessung!"
					XSize%=Int(Input("Deine Wahl:"))/2
					Print"waehle die y-Abmessung!"
					YSize%=Int(Input("Deine Wahl:"))/2
					Testobject1\CollisionBox = Sh_CreateSquare(-XSize,-YSize,XSize,YSize)
					Testobject1\Image = Ph_CreateImagefromCollisonBox(Testobject1\CollisionBox)
					Testobject1\RotMass = Ph_CalculateMomentOfInertia(Testobject1\CollisionBox,Testobject1\Mass)
					
			End Select
		EndIf
		MainPhysicTick((MilliSecs()-LastTime)/1000)
		LastTime=MilliSecs()
		MainPhysicRender()
		Flip
	Until KeyHit(1)
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D