import { IChild } from 'app/shared/model/child.model';

export interface IParent {
  id?: number;
  name?: string;
  children?: IChild[];
}

export class Parent implements IParent {
  constructor(public id?: number, public name?: string, public children?: IChild[]) {}
}
